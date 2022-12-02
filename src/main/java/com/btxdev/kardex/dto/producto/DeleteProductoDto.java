package com.btxdev.kardex.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.btxdev.kardex.entity.Producto} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteProductoDto {
    @NotNull
    private Long id;
}