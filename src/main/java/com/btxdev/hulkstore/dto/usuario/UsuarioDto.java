package com.btxdev.hulkstore.dto.usuario;

import com.btxdev.hulkstore.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * A DTO for the {@link com.btxdev.hulkstore.entity.Usuario} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto {
    private UUID id;
    private String nombre;
    private Usuario.Role rol;
    private String email;
    private String telefono;
}