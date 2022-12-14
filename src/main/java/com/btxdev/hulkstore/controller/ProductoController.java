package com.btxdev.hulkstore.controller;

import com.btxdev.hulkstore.dto.pagination.PageDto;
import com.btxdev.hulkstore.dto.producto.CreateProductoDto;
import com.btxdev.hulkstore.dto.producto.DeleteProductoDto;
import com.btxdev.hulkstore.dto.producto.ProductoDto;
import com.btxdev.hulkstore.dto.producto.UpdateProductoDto;
import com.btxdev.hulkstore.entity.Producto;
import com.btxdev.hulkstore.service.ProductoService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public PageDto<ProductoDto> get(@Filter Specification<Producto> spec, Pageable pageable
            , @RequestParam(required = false) String filter, @RequestParam(required = false) Integer page
            , @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort) {
        return productoService.read(spec, pageable);
    }

    @PostMapping
    public ProductoDto post(@RequestBody CreateProductoDto createProductoDto){
        return productoService.create(createProductoDto);
    }

    @PutMapping
    public ProductoDto put(@RequestBody UpdateProductoDto updateProductoDto){
        return productoService.update(updateProductoDto);
    }

    @DeleteMapping
    public void delete(@RequestBody DeleteProductoDto deleteProductoDto){
        productoService.delete(deleteProductoDto);
    }
}
