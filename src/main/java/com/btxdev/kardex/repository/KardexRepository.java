package com.btxdev.kardex.repository;

import com.btxdev.kardex.entity.Kardex;
import com.btxdev.kardex.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KardexRepository extends JpaRepository<Kardex, Long>, JpaSpecificationExecutor<Kardex> {

    List<Kardex> findByProductoOrderByFechaHoraAsc(Producto producto);

    Optional<Kardex> findFirstByProductoOrderByFechaHoraDesc(Producto producto);

    long deleteByProducto_Id(Long id);
}