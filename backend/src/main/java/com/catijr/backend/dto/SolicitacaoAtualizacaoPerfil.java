package com.catijr.backend.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SolicitacaoAtualizacaoPerfil {
    private String email;
    private String senhaAtual;
    private String novaSenha;
}
