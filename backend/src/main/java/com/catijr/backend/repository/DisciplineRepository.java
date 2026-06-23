package com.catijr.backend.repository;

import com.catijr.backend.model.DisciplineModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplineRepository extends JpaRepository<DisciplineModel, Long> {
    // Já tem findAll(), findById(), save(), delete()
}
