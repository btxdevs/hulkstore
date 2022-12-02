package com.btxdev.kardex.service;

import com.btxdev.kardex.component.Messages;
import com.btxdev.kardex.dto.kardex.RegistrarEntradaDto;
import com.btxdev.kardex.dto.kardex.RegistrarSalidaDto;
import com.btxdev.kardex.entity.*;
import com.btxdev.kardex.repository.KardexRepository;
import com.btxdev.kardex.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
public class KardexService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private KardexRepository kardexRepository;

    public void registrarEntrada(RegistrarEntradaDto registrarEntradaDto){
        Producto producto = productoRepository.findById(registrarEntradaDto.getId())
                .orElseThrow(() -> new EntityExistsException(Messages.PRODUCTO_NOT_FOUND));

        Kardex kardex = Kardex.builder()
                .fechaHora(LocalDateTime.now())
                .producto(producto)
                .detalle(registrarEntradaDto.getDetalle())
                .entrada(KardexEntrada.builder()
                        .cantidad(registrarEntradaDto.getCantidad())
                        .valorUnitario(registrarEntradaDto.getValorUnitario())
                        .valorTotal(registrarEntradaDto.getCantidad()*registrarEntradaDto.getValorUnitario())
                        .build())
                .build();

        Kardex lastKardexReg = kardexRepository.findFirstByOrderByFechaHoraDesc().orElse(null);

        Integer saldoCantidad = 0;
        Double saldoValorTotal = 0.0;

        if(lastKardexReg!=null){
            saldoCantidad = lastKardexReg.getSaldo().getCantidad();
            saldoValorTotal = lastKardexReg.getSaldo().getValorTotal();
        }

        Integer newSaldoCantidad = saldoCantidad+kardex.getEntrada().getCantidad();
        Double newSaldoValorTotal = saldoValorTotal+kardex.getEntrada().getValorTotal();
        Double newSaldoValorUnitario = newSaldoValorTotal/newSaldoCantidad;

        kardex.setSaldo(KardexSaldo.builder()
                        .cantidad(newSaldoCantidad)
                        .valorTotal(newSaldoValorTotal)
                        .valorUnitario(newSaldoValorUnitario)
                .build());

        kardexRepository.save(kardex);
    }

    public void registrarSalida(RegistrarSalidaDto registrarSalidaDto){
        Producto producto = productoRepository.findById(registrarSalidaDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(Messages.PRODUCTO_NOT_FOUND));

        Kardex kardex = Kardex.builder()
                .fechaHora(LocalDateTime.now())
                .producto(producto)
                .detalle(registrarSalidaDto.getDetalle())
                .salida(KardexSalida.builder()
                        .cantidad(registrarSalidaDto.getCantidad())
                        .valorUnitario(registrarSalidaDto.getValorUnitario())
                        .valorTotal(registrarSalidaDto.getCantidad()*registrarSalidaDto.getValorUnitario())
                        .build())
                .build();

        Kardex lastKardexReg = kardexRepository.findFirstByOrderByFechaHoraDesc().orElse(null);

        Integer saldoCantidad = 0;
        Double saldoValorTotal = 0.0;

        if(lastKardexReg!=null){
            saldoCantidad = lastKardexReg.getSaldo().getCantidad();
            saldoValorTotal = lastKardexReg.getSaldo().getValorTotal();
        }

        Integer newSaldoCantidad = saldoCantidad+kardex.getSalida().getCantidad();
        Double newSaldoValorTotal = saldoValorTotal+kardex.getSalida().getValorTotal();
        Double newSaldoValorUnitario = newSaldoValorTotal/newSaldoCantidad;

        kardex.setSaldo(KardexSaldo.builder()
                .cantidad(newSaldoCantidad)
                .valorTotal(newSaldoValorTotal)
                .valorUnitario(newSaldoValorUnitario)
                .build());

        kardexRepository.save(kardex);
    }
}
