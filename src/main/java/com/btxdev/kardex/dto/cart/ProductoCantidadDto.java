package com.btxdev.kardex.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoCantidadDto implements Serializable {
    private Long id;
    private Integer cantidad;
    private String nombre;
    private Long valorUnitario;
    private Long valorTotal;
}