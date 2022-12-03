package com.btxdev.kardex.dto.kardex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.btxdev.kardex.entity.KardexSalida} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KardexSalidaDto {
    private Long id;
    private Integer cantidad;
    private Long valorUnitario;
    private Long valorTotal;
}