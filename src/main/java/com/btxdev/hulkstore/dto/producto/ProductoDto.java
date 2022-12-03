package com.btxdev.hulkstore.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.btxdev.hulkstore.entity.Producto} entity
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDto {
    private Long id;
    private String nombre;
    private String sku;
    private Integer cantidadDisponible;
    private Long valorUnitario;
}