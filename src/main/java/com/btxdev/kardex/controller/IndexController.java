package com.btxdev.kardex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping("index")
    public String index(Model model){
        return "index";
    }

    @GetMapping("producto")
    public String producto(Model model){
        return "producto";
    }
}
