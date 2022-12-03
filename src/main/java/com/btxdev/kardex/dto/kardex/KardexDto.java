package com.btxdev.kardex.dto.kardex;

import com.btxdev.kardex.dto.producto.ProductoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.btxdev.kardex.entity.Kardex} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KardexDto {
    private Long id;
    private LocalDateTime fechaHora;
    private ProductoDto producto;
    private String detalle;
    private KardexEntradaDto entrada;
    private KardexSalidaDto salida;
    private KardexSaldoDto saldo;
}