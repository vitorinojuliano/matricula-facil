package com.catijr.backend.repository;

import com.catijr.backend.model.DisciplinaModelo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioDisciplina extends JpaRepository<DisciplinaModelo, Long> {
}
