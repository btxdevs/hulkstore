package com.btxdev.kardex.service;

import com.btxdev.kardex.component.Converter;
import com.btxdev.kardex.component.Messages;
import com.btxdev.kardex.component.Validator;
import com.btxdev.kardex.dto.PageDto;
import com.btxdev.kardex.dto.producto.CreateProductoDto;
import com.btxdev.kardex.dto.producto.DeleteProductoDto;
import com.btxdev.kardex.dto.producto.ProductoDto;
import com.btxdev.kardex.dto.producto.UpdateProductoDto;
import com.btxdev.kardex.entity.Producto;
import com.btxdev.kardex.repository.ProductoRepository;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Log4j2
@Service
public class ProductoService {

    @Autowired
    private Validator validator;
    @Autowired
    private Converter converter;
    @Autowired
    private ProductoRepository productoRepository;

    public ProductoDto create(@NonNull CreateProductoDto createProductoDto){
        validator.validate(createProductoDto);
        if(productoRepository.existsBySku(createProductoDto.getSku())){
            throw new EntityExistsException(Messages.PRODUCTO_SKU_ALREADY_EXISTS);
        }
        Producto producto = converter.map(createProductoDto, Producto.class);
        productoRepository.save(producto);
        return converter.map(producto, ProductoDto.class);
    }

    public PageDto<ProductoDto> read(Specification<Producto> spec, @NonNull Pageable pageable){
        Page<Producto> page = productoRepository.findAll(spec, pageable);
        PageDto<ProductoDto> pageDto = new PageDto<>();
        pageDto.setElements(converter.mapList(page.getContent(), ProductoDto.class));
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());
        return pageDto;
    }

    public ProductoDto update(@NonNull UpdateProductoDto updateProductoDto){
        validator.validate(updateProductoDto);
        if(!productoRepository.existsById(updateProductoDto.getId())){
            throw new EntityNotFoundException(Messages.PRODUCTO_NOT_FOUND);
        }

        Producto producto = converter.map(updateProductoDto, Producto.class);

        productoRepository.save(producto);

        return converter.map(producto, ProductoDto.class);
    }

    public void delete(@NonNull DeleteProductoDto deleteProductoDto){
        validator.validate(deleteProductoDto);
        if(!productoRepository.existsById(deleteProductoDto.getId())){
            throw new EntityNotFoundException(Messages.PRODUCTO_NOT_FOUND);
        }
        productoRepository.deleteById(deleteProductoDto.getId());
    }
}
