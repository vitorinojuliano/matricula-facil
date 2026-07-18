package com.catijr.backend.repository;

import com.catijr.backend.model.UsuarioModelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositorioUsuario extends JpaRepository<UsuarioModelo, Long> {
    Optional<UsuarioModelo> findByEmail(String email);
}
