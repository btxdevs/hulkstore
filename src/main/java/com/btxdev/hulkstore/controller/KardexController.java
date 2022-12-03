package com.btxdev.hulkstore.controller;

import com.btxdev.hulkstore.dto.pagination.PageDto;
import com.btxdev.hulkstore.dto.kardex.KardexDto;
import com.btxdev.hulkstore.dto.kardex.RegistrarEntradaDto;
import com.btxdev.hulkstore.dto.kardex.RegistrarSalidaDto;
import com.btxdev.hulkstore.entity.Kardex;
import com.btxdev.hulkstore.service.KardexService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/kardex")
public class KardexController {

    @Autowired
    private KardexService kardexService;

    @PostMapping("registrarEntrada")
    public KardexDto registrarEntrada(@RequestBody RegistrarEntradaDto registrarEntradaDto){
        return kardexService.registrarEntrada(registrarEntradaDto);
    }

    @PostMapping("registrarSalida")
    public KardexDto registrarSalida(@RequestBody RegistrarSalidaDto registrarSalidaDto){
        return kardexService.registrarSalida(registrarSalidaDto);
    }

    @GetMapping
    public PageDto<KardexDto> get(@Filter Specification<Kardex> spec, Pageable pageable
            , @RequestParam(required = false) String filter, @RequestParam(required = false) Integer page
            , @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort) {
        return kardexService.read(spec, pageable);
    }
}
