package com.btxdev.hulkstore.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.btxdev.hulkstore.entity.Producto} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductoDto {
    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String nombre;
    @NotBlank
    @Size(max = 50)
    private String sku;
}