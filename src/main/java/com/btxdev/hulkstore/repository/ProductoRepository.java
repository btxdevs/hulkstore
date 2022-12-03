package com.btxdev.hulkstore.repository;

import com.btxdev.hulkstore.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    boolean existsBySku(String sku);

    Optional<Producto> findBySku(String sku);



}