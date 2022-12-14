package com.btxdev.hulkstore.repository;

import com.btxdev.hulkstore.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>, JpaSpecificationExecutor<Usuario> {
    boolean existsByNombre(String nombre);

    Optional<Usuario> findByNombre(String nombre);

    long deleteByNombre(String nombre);


}