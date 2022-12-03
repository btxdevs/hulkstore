package com.btxdev.hulkstore.dto.usuario;

import com.btxdev.hulkstore.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * A DTO for the {@link Usuario} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteUsuarioDto {
    @NotEmpty
    private String nombre;
}