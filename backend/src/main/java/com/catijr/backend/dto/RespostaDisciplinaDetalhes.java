package com.catijr.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RespostaDisciplinaDetalhes extends RespostaDisciplinaCard{
    private String professor;
    private String descricao;
    private List<PreRequisitoDTO> preRequisitosDetalhados;
}
