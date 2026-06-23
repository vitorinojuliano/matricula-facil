package com.catijr.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
// Nome, Código, Créditos, Vagas, Horário e Status de Pré-requisito.
@Entity
public class DisciplineModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;
    @Column(nullable = false, unique = true)
    private String codigo;
    @Column(nullable = false, length = 255)
    private Integer creditos;
    @Column(nullable = false, length = 255)
    private Integer vagas;
    @Column(nullable = false, length = 255)
    private String horario;
    @Column(nullable = false, length = 255)
    private String status;
    @Column(nullable = false, length = 255)
    private String pre_requisito;

}
