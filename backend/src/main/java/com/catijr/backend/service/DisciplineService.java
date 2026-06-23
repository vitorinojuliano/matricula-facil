package com.catijr.backend.service;

import com.catijr.backend.model.DisciplineModel;
import com.catijr.backend.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineService {
    @Autowired
    private DisciplineRepository disciplineRepository;

    public List<DisciplineModel> listDisciplines(){
        return disciplineRepository.findAll();
    }
}
