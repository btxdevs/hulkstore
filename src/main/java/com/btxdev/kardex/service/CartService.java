package com.btxdev.kardex.service;

import com.btxdev.kardex.component.Messages;
import com.btxdev.kardex.dto.cart.CartDto;
import com.btxdev.kardex.dto.cart.ProductoCantidadDto;
import com.btxdev.kardex.dto.kardex.RegistrarSalidaDto;
import com.btxdev.kardex.dto.producto.ProductoDto;
import com.btxdev.kardex.entity.Producto;
import com.btxdev.kardex.repository.ProductoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@Log4j2
public class CartService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private KardexService kardexService;

    public CartDto add(ProductoCantidadDto productoCantidadDto){
        HttpSession session = getSession();
        CartDto cartDto = (CartDto) session.getAttribute("cart");
        if(cartDto==null){
            cartDto = new CartDto();
        }

        Producto producto = productoRepository.findById(productoCantidadDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(Messages.PRODUCTO_NOT_FOUND));

        Optional<ProductoCantidadDto> opc = cartDto.getProductos().stream()
                .filter(p -> p.getId().equals(productoCantidadDto.getId())).findAny();

        if(opc.isPresent()){
            ProductoCantidadDto pc = opc.get();
            if(pc.getCantidad()+productoCantidadDto.getCantidad()>producto.getCantidadDisponible()){
                throw new IllegalArgumentException(Messages.PRODUCTO_CANTIDAD_INSUFICIENTE);
            }
            pc.setCantidad(pc.getCantidad()+ productoCantidadDto.getCantidad());
            pc.setValorTotal(pc.getValorUnitario()
                    *pc.getCantidad());
        }else{
            if(productoCantidadDto.getCantidad()>producto.getCantidadDisponible()){
                throw new IllegalArgumentException(Messages.PRODUCTO_CANTIDAD_INSUFICIENTE);
            }
            productoCantidadDto.setNombre(producto.getNombre());
            productoCantidadDto.setValorUnitario(producto.getValorUnitario());
            productoCantidadDto.setValorTotal(productoCantidadDto.getValorUnitario()
                    *productoCantidadDto.getCantidad());
            cartDto.getProductos().add(productoCantidadDto);
        }

        cartDto.setCantidadTotal(cartDto.getCantidadTotal()+ productoCantidadDto.getCantidad());
        cartDto.setValorTotal(cartDto.getProductos().stream()
                .map(ProductoCantidadDto::getValorTotal).reduce(0L, Long::sum));

        session.setAttribute("cart", cartDto);
        return cartDto;
    }

    public CartDto remove(ProductoCantidadDto productoCantidadDto){
        HttpSession session = getSession();
        CartDto cartDto = (CartDto) session.getAttribute("cart");
        if(cartDto==null){
            cartDto = new CartDto();
        }

        Optional<ProductoCantidadDto> opc = cartDto.getProductos().stream()
                .filter(p -> p.getId().equals(productoCantidadDto.getId())).findAny();

        if(opc.isPresent()){
            ProductoCantidadDto pc = opc.get();
            if(pc.getCantidad()<= productoCantidadDto.getCantidad()){
                cartDto.getProductos().remove(pc);
                cartDto.setCantidadTotal(cartDto.getCantidadTotal()-pc.getCantidad());
            }else{
                pc.setCantidad(pc.getCantidad()- productoCantidadDto.getCantidad());
                pc.setValorTotal(pc.getValorUnitario()
                        *pc.getCantidad());
                cartDto.setCantidadTotal(cartDto.getCantidadTotal()- productoCantidadDto.getCantidad());
            }
        }

        cartDto.setValorTotal(cartDto.getProductos().stream()
                .map(ProductoCantidadDto::getValorTotal).reduce(0L, Long::sum));

        session.setAttribute("cart", cartDto);
        return cartDto;
    }

    public CartDto get(){
        HttpSession session = getSession();
        CartDto cartDto = (CartDto) session.getAttribute("cart");
        if(cartDto==null){
            cartDto = new CartDto();
        }
        return cartDto;
    }

    @Transactional
    public void checkOut(){
        HttpSession session = getSession();
        CartDto cartDto = (CartDto) session.getAttribute("cart");
        if(cartDto==null){
            cartDto = new CartDto();
        }

        if(cartDto.getCantidadTotal()==0){
            throw new IllegalArgumentException(Messages.CART_EMPTY);
        }

        for(ProductoCantidadDto productoCantidadDto: cartDto.getProductos()){
            RegistrarSalidaDto registrarSalidaDto = RegistrarSalidaDto.builder()
                    .id(productoCantidadDto.getId())
                    .cantidad(productoCantidadDto.getCantidad())
                    .detalle("Venta")
                    .build();
            kardexService.registrarSalida(registrarSalidaDto);
        }

        session.setAttribute("cart", new CartDto());
    }

    private HttpSession getSession(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}
