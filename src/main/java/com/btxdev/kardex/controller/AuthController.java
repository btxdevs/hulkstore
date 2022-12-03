package com.btxdev.kardex.controller;

import com.btxdev.kardex.dto.login.LoginDto;
import com.btxdev.kardex.dto.usuario.UsuarioDto;
import com.btxdev.kardex.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("login")
    public void login(@RequestBody LoginDto loginDto, HttpSession session){
        UsuarioDto user = authService.login(loginDto);
        session.setAttribute("username", user.getNombre());
    }
}
