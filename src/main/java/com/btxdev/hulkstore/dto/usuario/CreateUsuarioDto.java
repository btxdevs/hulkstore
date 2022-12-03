package com.btxdev.hulkstore.dto.usuario;

import com.btxdev.hulkstore.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link Usuario} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUsuarioDto {
    @NotBlank
    @Size(max = 20)
    private String nombre;
    @NotNull
    private Usuario.Role rol;
    @NotBlank
    @Size(max = 50)
    private String email;
    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "\\d+")
    private String telefono;
    @NotBlank
    @Size(max = 30)
    private String password;
}