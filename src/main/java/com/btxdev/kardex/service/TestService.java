package com.btxdev.kardex.service;

import com.btxdev.kardex.repository.KardexRepository;
import com.btxdev.kardex.repository.ProductoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Log4j2
@Service
public class TestService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private KardexRepository kardexRepository;

    @PostConstruct
    private void init(){

       /* if(!productoRepository.existsBySku("111222333")){
            Producto producto = Producto.builder()
                    .nombre("Gaseosa Cocacola")
                    .sku("111222333")
                    .build();

            productoRepository.save(producto);

            log.info(producto);
        }

        Producto producto = productoRepository.findBySku("111222333").orElse(null);*/

      /*  Kardex kardex0 = Kardex.builder()
                .fecha(LocalDate.of(2020, 3, 1))
                .producto(producto)
                .detalle("Compras")
                .entrada(KardexEntrada.builder()
                        .cantidad(5)
                        .valorUnitario(1000.0)
                        .valorTotal(5000.0)
                        .build())
                .build();

        Kardex kardex1 = Kardex.builder()
                .fecha(LocalDate.of(2020, 3, 7))
                .producto(producto)
                .detalle("Ventas")
                .salida(KardexSalida.builder()
                        .cantidad(7)
                        .valorUnitario(891.0)
                        .valorTotal(6237.0)
                        .build())
                .build();


        Kardex kardex = Kardex.builder()
                .fecha(LocalDate.of(2020, 3, 6))
                .producto(producto)
                .detalle("Compras")
                .entrada(KardexEntrada.builder()
                        .cantidad(6)
                        .valorUnitario(800.0)
                        .valorTotal(4800.0)
                        .build())
                .build();

        kardexRepository.save(kardex);
        kardexRepository.save(kardex1);
        kardexRepository.save(kardex0);*/

       /* List<Kardex> kardexList = kardexRepository.findByProductoOrderByFechaAsc(producto);

        int cantidad = 0;
        double valorUnitario = 0.0;
        double valorTotal = 0.0;
        for (Kardex k : kardexList){
            KardexEntrada kardexEntrada = k.getEntrada();
            KardexSalida kardexSalida = k.getSalida();
            if(kardexEntrada!=null){
                cantidad+=kardexEntrada.getCantidad();
                valorTotal+=kardexEntrada.getValorTotal();
                valorUnitario=valorTotal/cantidad;
            }
            if(kardexSalida!=null){
                cantidad-=kardexSalida.getCantidad();
                valorTotal-=kardexSalida.getValorTotal();
                valorUnitario=valorTotal/cantidad;
            }
            k.setSaldo(KardexSaldo.builder()
                    .cantidad(cantidad)
                    .valorUnitario(valorUnitario)
                    .valorTotal(valorTotal)
                    .build());

            log.info(k);
        }*/
    }
}
