package com.btxdev.hulkstore.service;

import com.btxdev.hulkstore.component.Converter;
import com.btxdev.hulkstore.component.Validator;
import com.btxdev.hulkstore.dto.pagination.PageDto;
import com.btxdev.hulkstore.dto.producto.CreateProductoDto;
import com.btxdev.hulkstore.dto.producto.DeleteProductoDto;
import com.btxdev.hulkstore.dto.producto.ProductoDto;
import com.btxdev.hulkstore.dto.producto.UpdateProductoDto;
import com.btxdev.hulkstore.entity.Producto;
import com.btxdev.hulkstore.repository.ProductoRepository;
import com.turkraft.springfilter.boot.FilterSpecification;
import com.turkraft.springfilter.parser.Filter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {


    @Mock
    private ProductoRepository productoRepository;

    @Spy
    private Validator validator;

    @Spy
    private Converter converter;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void create() {

        CreateProductoDto createProductoDto = CreateProductoDto.builder()
                .nombre("nombre")
                .sku("sku")
                .build();

        Long id = 1L;

        when(productoRepository.existsBySku(any())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> productoService.create(createProductoDto));

        when(productoRepository.existsBySku(any())).thenReturn(false);
        doAnswer((InvocationOnMock invocation) -> {
            Producto o = (Producto) invocation.getArguments()[0];
            o.setId(id);
            return null;
        }).when(productoRepository).save(any(Producto.class));

        ProductoDto productoDto = productoService.create(createProductoDto);

        assertEquals(id, productoDto.getId());
    }

    @Test
    void read() {
        Pageable pageable = PageRequest.of(0, 10);

        when(productoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(Producto.builder().build())));

        Filter filter = Filter.from("nombre:'test'");
        Specification<Producto> spec = new FilterSpecification<>(filter);

        PageDto<ProductoDto> pageDto = productoService.read(spec, pageable);

        assertEquals(1, pageDto.getElements().size());
    }

    @Test
    void update() {
        String nombre = "nombre";
        UpdateProductoDto updateProductoDto = UpdateProductoDto.builder()
                .nombre(nombre)
                .sku("sku")
                .id(1L)
                .build();

        when(productoRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productoService.update(updateProductoDto));

        Producto producto = Producto.builder()
                .id(updateProductoDto.getId())
                .nombre(updateProductoDto.getNombre())
                .sku(updateProductoDto.getSku())
                .build();

        when(productoRepository.existsById(any())).thenReturn(true);
        when(productoRepository.save(any())).thenReturn(producto);

        ProductoDto productoDto = productoService.update(updateProductoDto);

        assertEquals(nombre, productoDto.getNombre());
    }

    @Test
    void delete() {
        DeleteProductoDto deleteProductoDto = new DeleteProductoDto(1L);

        when(productoRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productoService.delete(deleteProductoDto));

        when(productoRepository.existsById(any())).thenReturn(true);

        assertDoesNotThrow(() -> productoService.delete(deleteProductoDto));
    }
}