package com.btxdev.kardex.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.btxdev.kardex.entity.Producto} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductoDto {
    @NotBlank
    @Size(max = 50)
    private String nombre;
    @NotBlank
    @Size(max = 50)
    private String sku;
}