package com.btxdev.kardex.dto.kardex;

import lombok.Data;

@Data
public class RegistrarSalidaDto {
    private Long id;
    private String detalle;
    private Integer cantidad;
    private Double valorUnitario;
}
