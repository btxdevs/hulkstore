package com.btxdev.kardex.dto.usuario;

import com.btxdev.kardex.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.btxdev.kardex.entity.Usuario} entity
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