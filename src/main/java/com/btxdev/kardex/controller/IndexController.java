package com.btxdev.kardex.controller;

import com.btxdev.kardex.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private AuthService authService;

    @GetMapping("login")
    public String login(@RequestParam(required = false) boolean logout){
        if(logout){
            authService.logout();
            return "redirect:shop";
        }else{
            if(authService.isAuthenticated()){
                return "redirect:shop";
            }
            return "login";
        }
    }

    @GetMapping("shop")
    public String index(Model model){
        return "shop";
    }

    @GetMapping("cart")
    public String cart(Model model){
        return "cart";
    }

    @GetMapping("usuario")
    public String usuario(Model model){
        return "usuario";
    }

    @GetMapping("producto")
    public String producto(Model model){
        return "producto";
    }

    @GetMapping("kardex")
    public String kardex(@RequestParam String idProducto, Model model){
        model.addAttribute("idProducto", idProducto);
        return "kardex";
    }
}
