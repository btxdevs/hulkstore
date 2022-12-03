package com.btxdev.hulkstore.dto.kardex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrarSalidaDto {
    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String detalle;
    @NotNull
    @Min(1)
    private Integer cantidad;
}
