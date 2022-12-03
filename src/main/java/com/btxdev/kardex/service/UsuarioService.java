package com.btxdev.kardex.service;

import com.btxdev.kardex.component.Converter;
import com.btxdev.kardex.component.Messages;
import com.btxdev.kardex.component.Validator;
import com.btxdev.kardex.dto.pagination.PageDto;
import com.btxdev.kardex.dto.usuario.CreateUsuarioDto;
import com.btxdev.kardex.dto.usuario.DeleteUsuarioDto;
import com.btxdev.kardex.dto.usuario.UpdateUsuarioDto;
import com.btxdev.kardex.dto.usuario.UsuarioDto;
import com.btxdev.kardex.entity.Usuario;
import com.btxdev.kardex.repository.UsuarioRepository;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@Log4j2
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator validator;

    @Autowired
    private Converter converter;

    @Transactional
    public UsuarioDto create(CreateUsuarioDto createUsuarioDto){
        validator.validate(createUsuarioDto);

        if(usuarioRepository.existsByNombre(createUsuarioDto.getNombre())){
            throw new EntityExistsException(Messages.USUARIO_NOMBRE_ALREADY_EXISTS);
        }

        Usuario usuario = converter.map(createUsuarioDto, Usuario.class);

        usuario.setPasswordHash(passwordEncoder.encode(createUsuarioDto.getPassword()));

        usuarioRepository.save(usuario);

        return converter.map(usuario, UsuarioDto.class);
    }

    public PageDto<UsuarioDto> read(Specification<Usuario> spec, @NonNull Pageable pageable){
        Page<Usuario> page = usuarioRepository.findAll(spec, pageable);
        PageDto<UsuarioDto> pageDto = new PageDto<>();
        pageDto.setElements(converter.mapList(page.getContent(), UsuarioDto.class));
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());
        return pageDto;
    }

    @Transactional
    public UsuarioDto update(UpdateUsuarioDto updateUsuarioDto){
        validator.validate(updateUsuarioDto);

        Usuario usuario = usuarioRepository.findByNombre(updateUsuarioDto.getNombre())
                .orElseThrow(() -> new EntityNotFoundException(Messages.USUARIO_NOT_FOUND));

        updateUsuarioDto.getRol().ifPresent(usuario::setRol);
        updateUsuarioDto.getEmail().ifPresent(usuario::setEmail);
        updateUsuarioDto.getTelefono().ifPresent(usuario::setTelefono);
        updateUsuarioDto.getPassword().ifPresent(p -> usuario.setPasswordHash(passwordEncoder.encode(p)));

        usuarioRepository.save(usuario);

        return converter.map(usuario, UsuarioDto.class);
    }

    @Transactional
    public void delete(DeleteUsuarioDto deleteUsuarioDto){
        validator.validate(deleteUsuarioDto);

        if(!usuarioRepository.existsByNombre(deleteUsuarioDto.getNombre())){
            throw new EntityNotFoundException(Messages.USUARIO_NOT_FOUND);
        }

        usuarioRepository.deleteByNombre(deleteUsuarioDto.getNombre());
    }
}
