package com.btxdev.kardex.service;

import com.btxdev.kardex.component.Converter;
import com.btxdev.kardex.component.Messages;
import com.btxdev.kardex.component.Validator;
import com.btxdev.kardex.dto.login.LoginDto;
import com.btxdev.kardex.dto.usuario.UsuarioDto;
import com.btxdev.kardex.entity.Usuario;
import com.btxdev.kardex.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator validator;

    @Autowired
    private Converter converter;
    public UsuarioDto login(LoginDto loginDto){
        validator.validate(loginDto);
        Usuario usuario = usuarioRepository.findByNombre(loginDto.getName())
                .orElseThrow(() -> new SecurityException(Messages.LOGIN_FAILED));

        if(!passwordEncoder.matches(loginDto.getPassword(), usuario.getPasswordHash())){
            throw new SecurityException(Messages.LOGIN_FAILED);
        }

        UsuarioDto usuarioDto = converter.map(usuario, UsuarioDto.class);

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(usuarioDto, null
                , Set.of(new SimpleGrantedAuthority(usuario.getRol().name())));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        return usuarioDto;
    }

    public boolean isAuthenticated(){
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken);
    }

    public UsuarioDto getAuthenticatedUser(){
        if(isAuthenticated()){
            return (UsuarioDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    public void logout(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        if(session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }
}
