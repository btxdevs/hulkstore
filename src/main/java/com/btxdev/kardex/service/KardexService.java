package com.btxdev.kardex.service;

import com.btxdev.kardex.component.Converter;
import com.btxdev.kardex.component.Messages;
import com.btxdev.kardex.component.Validator;
import com.btxdev.kardex.dto.pagination.PageDto;
import com.btxdev.kardex.dto.kardex.KardexDto;
import com.btxdev.kardex.dto.kardex.RegistrarEntradaDto;
import com.btxdev.kardex.dto.kardex.RegistrarSalidaDto;
import com.btxdev.kardex.entity.*;
import com.btxdev.kardex.repository.KardexRepository;
import com.btxdev.kardex.repository.ProductoRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
public class KardexService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private KardexRepository kardexRepository;

    @Autowired
    private Converter converter;

    @Autowired
    private Validator validator;

    @Transactional
    public KardexDto registrarEntrada(RegistrarEntradaDto registrarEntradaDto){
        validator.validate(registrarEntradaDto);
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

        Kardex lastKardexReg = kardexRepository.findFirstByProductoOrderByFechaHoraDesc(producto)
                .orElse(null);

        Integer saldoCantidad = 0;
        Long saldoValorTotal = 0L;

        if(lastKardexReg!=null){
            saldoCantidad = lastKardexReg.getSaldo().getCantidad();
            saldoValorTotal = lastKardexReg.getSaldo().getValorTotal();
        }

        Integer newSaldoCantidad = saldoCantidad+kardex.getEntrada().getCantidad();
        Long newSaldoValorTotal = saldoValorTotal+kardex.getEntrada().getValorTotal();
        Long newSaldoValorUnitario = newSaldoValorTotal/newSaldoCantidad;

        producto.setCantidadDisponible(newSaldoCantidad);
        producto.setValorUnitario(newSaldoValorUnitario);

        kardex.setSaldo(KardexSaldo.builder()
                        .cantidad(newSaldoCantidad)
                        .valorTotal(newSaldoValorTotal)
                        .valorUnitario(newSaldoValorUnitario)
                .build());

        kardexRepository.save(kardex);

        return converter.map(kardex, KardexDto.class);
    }

    @Transactional
    public KardexDto registrarSalida(RegistrarSalidaDto registrarSalidaDto){
        validator.validate(registrarSalidaDto);
        Producto producto = productoRepository.findById(registrarSalidaDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(Messages.PRODUCTO_NOT_FOUND));

        Kardex lastKardexReg = kardexRepository.findFirstByProductoOrderByFechaHoraDesc(producto)
                .orElse(null);

        Integer saldoCantidad = 0;
        Long saldoValorTotal = 0L;
        Long saldoValorUnitario = 0L;

        if(lastKardexReg!=null){
            saldoCantidad = lastKardexReg.getSaldo().getCantidad();
            saldoValorTotal = lastKardexReg.getSaldo().getValorTotal();
            saldoValorUnitario = lastKardexReg.getSaldo().getValorUnitario();
        }

        if(saldoCantidad<registrarSalidaDto.getCantidad()){
            throw new IllegalArgumentException(Messages.PRODUCTO_CANTIDAD_INSUFICIENTE);
        }

        Kardex kardex = Kardex.builder()
                .fechaHora(LocalDateTime.now())
                .producto(producto)
                .detalle(registrarSalidaDto.getDetalle())
                .salida(KardexSalida.builder()
                        .cantidad(registrarSalidaDto.getCantidad())
                        .valorUnitario(saldoValorUnitario)
                        .valorTotal(saldoValorUnitario*registrarSalidaDto.getCantidad())
                        .build())
                .build();

        Integer newSaldoCantidad = saldoCantidad-kardex.getSalida().getCantidad();
        Long newSaldoValorTotal = saldoValorTotal-kardex.getSalida().getValorTotal();
        Long newSaldoValorUnitario = newSaldoValorTotal/newSaldoCantidad;

        producto.setCantidadDisponible(newSaldoCantidad);
        producto.setValorUnitario(newSaldoValorUnitario);

        kardex.setSaldo(KardexSaldo.builder()
                .cantidad(newSaldoCantidad)
                .valorTotal(newSaldoValorTotal)
                .valorUnitario(newSaldoValorUnitario)
                .build());

        kardexRepository.save(kardex);

        return converter.map(kardex, KardexDto.class);
    }

    public PageDto<KardexDto> read(Specification<Kardex> spec, @NonNull Pageable pageable){
        Page<Kardex> page = kardexRepository.findAll(spec, pageable);
        PageDto<KardexDto> pageDto = new PageDto<>();
        pageDto.setElements(converter.mapList(page.getContent(), KardexDto.class));
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());
        return pageDto;
    }
}
