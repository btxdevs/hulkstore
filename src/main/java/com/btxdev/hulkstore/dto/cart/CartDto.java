package com.btxdev.hulkstore.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto implements Serializable {

    private List<ProductoCantidadDto> productos = new ArrayList<>();

    private Integer cantidadTotal = 0;

    private Long valorTotal = 0L;
}
