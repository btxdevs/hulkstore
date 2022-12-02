package com.btxdev.kardex.dto.kardex;

import lombok.Data;

@Data
public class RegistrarEntradaDto {
    private Long id;
    private String detalle;
    private Integer cantidad;
    private Double valorUnitario;
}
