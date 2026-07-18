package com.catijr.backend.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor

@Entity
public class MatriculaModelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="usuario_id", nullable = false)
    private UsuarioModelo usuario;

    @ManyToOne
    @JoinColumn(name="disciplina_id", nullable = false)
    private DisciplinaModelo disciplina;

    @Column
    private LocalDateTime dataMatricula;

    @Column(nullable = false, length = 100)
    private String status;


}
