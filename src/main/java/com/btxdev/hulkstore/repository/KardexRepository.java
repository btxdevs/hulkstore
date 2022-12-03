package com.btxdev.hulkstore.repository;

import com.btxdev.hulkstore.entity.Kardex;
import com.btxdev.hulkstore.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface KardexRepository extends JpaRepository<Kardex, Long>, JpaSpecificationExecutor<Kardex> {

    List<Kardex> findByProductoOrderByFechaHoraAsc(Producto producto);

    Optional<Kardex> findFirstByProductoOrderByFechaHoraDesc(Producto producto);

    long deleteByProducto_Id(Long id);
}