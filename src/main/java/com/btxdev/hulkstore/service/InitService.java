package com.btxdev.hulkstore.service;

import com.btxdev.hulkstore.entity.Usuario;
import com.btxdev.hulkstore.repository.KardexRepository;
import com.btxdev.hulkstore.repository.UsuarioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Log4j2
@Service
public class InitService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private KardexRepository kardexRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init(){
        Usuario usuario = Usuario.builder()
                .nombre("admin")
                .telefono("9999999999")
                .email("admin@hulkstore.com")
                .rol(Usuario.Role.ADMIN)
                .passwordHash(passwordEncoder.encode("admin"))
                .build();

        if(!usuarioRepository.existsByNombre(usuario.getNombre())){
            usuarioRepository.save(usuario);
        }
    }
}
