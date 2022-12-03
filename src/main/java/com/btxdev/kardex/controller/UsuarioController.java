package com.btxdev.kardex.controller;

import com.btxdev.kardex.dto.pagination.PageDto;
import com.btxdev.kardex.dto.usuario.CreateUsuarioDto;
import com.btxdev.kardex.dto.usuario.DeleteUsuarioDto;
import com.btxdev.kardex.dto.usuario.UpdateUsuarioDto;
import com.btxdev.kardex.dto.usuario.UsuarioDto;
import com.btxdev.kardex.entity.Usuario;
import com.btxdev.kardex.service.UsuarioService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public PageDto<UsuarioDto> get(@Filter Specification<Usuario> spec, Pageable pageable
            , @RequestParam(required = false) String filter, @RequestParam(required = false) Integer page
            , @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort) {
        return usuarioService.read(spec, pageable);
    }

    @PostMapping
    public UsuarioDto post(@RequestBody CreateUsuarioDto createUsuarioDto){
        return usuarioService.create(createUsuarioDto);
    }

    @PatchMapping
    public UsuarioDto patch(@RequestBody UpdateUsuarioDto updateUsuarioDto){
        return usuarioService.update(updateUsuarioDto);
    }

    @DeleteMapping
    public void delete(@RequestBody DeleteUsuarioDto deleteUsuarioDto){
        usuarioService.delete(deleteUsuarioDto);
    }
}
