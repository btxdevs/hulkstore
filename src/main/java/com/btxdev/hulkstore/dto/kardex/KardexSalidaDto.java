package com.btxdev.hulkstore.dto.kardex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.btxdev.hulkstore.entity.KardexSalida} entity
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