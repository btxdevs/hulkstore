package com.btxdev.hulkstore.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.btxdev.hulkstore.entity.Producto} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteProductoDto {
    @NotNull
    private Long id;
}