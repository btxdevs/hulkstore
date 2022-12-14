package com.btxdev.hulkstore.controller;

import com.btxdev.hulkstore.dto.cart.CartDto;
import com.btxdev.hulkstore.dto.cart.ProductoCantidadDto;
import com.btxdev.hulkstore.service.CartService;
import com.btxdev.hulkstore.service.KardexService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
@Log4j2
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private KardexService kardexService;

    @PostMapping
    public CartDto add(@RequestBody ProductoCantidadDto productoCantidadDto){
        return cartService.add(productoCantidadDto);
    }

    @DeleteMapping
    public CartDto remove(@RequestBody ProductoCantidadDto productoCantidadDto){
        return cartService.remove(productoCantidadDto);
    }

    @GetMapping
    public CartDto get(){
        return cartService.get();
    }

    @PostMapping("checkout")
    public void checkOut(){
        cartService.checkOut();
    }
}
