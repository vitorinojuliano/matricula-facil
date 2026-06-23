package com.catijr.backend.controller;

import com.catijr.backend.model.DisciplineModel;
import com.catijr.backend.repository.DisciplineRepository;
import com.catijr.backend.service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/MatriculaFacil")
public class DisciplineController {

    @Autowired
    private DisciplineService disciplineService;

    @GetMapping("/disciplines")
    public ResponseEntity<List<DisciplineModel>> listDisciplines(){
        List<DisciplineModel> discipline =  disciplineService.listDisciplines();
        return ResponseEntity.ok(discipline);
    }
}
