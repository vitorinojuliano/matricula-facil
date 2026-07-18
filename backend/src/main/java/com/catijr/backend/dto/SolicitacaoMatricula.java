package com.catijr.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SolicitacaoMatricula {

    @NotNull(message = "Disciplina é obrigatória")
    @Positive(message = "Disciplina inválida")
    private Long disciplinaId;
}
