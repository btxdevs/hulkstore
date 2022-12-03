package com.btxdev.kardex.dto.usuario;

import com.btxdev.kardex.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * A DTO for the {@link Usuario} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUsuarioDto {
    @NotBlank
    private String nombre;
    private JsonNullable<Usuario.Role> rol = JsonNullable.undefined();
    @Size(max = 50)
    private JsonNullable<String> email = JsonNullable.undefined();
    @Size(max = 20)
    @Pattern(regexp = "\\d+")
    private JsonNullable<String> telefono = JsonNullable.undefined();
    @Size(max = 30)
    private JsonNullable<String> password = JsonNullable.undefined();
}