package com.catijr.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RespostaDisciplinaCard {
    private Long  id;
    private String nome;
    private String codigo;
    private Integer creditos;
    private Integer vagas;
    private String horario;
    private String status;
    private Integer semestre;
    private Integer ano;
    private String preRequisitoCodigo;
    private String preRequisitoStatus;
    private Long matriculaId;
    private String matriculaStatus;
}
