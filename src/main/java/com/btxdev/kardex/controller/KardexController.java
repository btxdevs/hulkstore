package com.btxdev.kardex.controller;

import com.btxdev.kardex.dto.pagination.PageDto;
import com.btxdev.kardex.dto.kardex.KardexDto;
import com.btxdev.kardex.dto.kardex.RegistrarEntradaDto;
import com.btxdev.kardex.dto.kardex.RegistrarSalidaDto;
import com.btxdev.kardex.entity.Kardex;
import com.btxdev.kardex.service.KardexService;
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
