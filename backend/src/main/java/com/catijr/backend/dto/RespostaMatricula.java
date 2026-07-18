package com.catijr.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class RespostaMatricula  {
    private Long id;
    private String nomeDaMatricula;
    private String codigoDaDisciplina;
    private Integer creditos;
    private String horario;
    private String status;
    private LocalDateTime dataMatricula;

    private Integer semestre;
    private Integer ano;
}
