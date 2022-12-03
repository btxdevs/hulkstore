package com.btxdev.kardex.dto.usuario;

import com.btxdev.kardex.entity.Usuario;
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