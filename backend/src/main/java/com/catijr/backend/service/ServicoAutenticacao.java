package com.catijr.backend.service;

import com.catijr.backend.exception.ExcecaoUsuarioNaoEncontrado;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.repository.RepositorioUsuario;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoAutenticacao {
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public UsuarioModelo getUsuarioAutenticado(Authentication autenticacao) {
        String email = autenticacao.getName();
        return repositorioUsuario.findByEmail(email)
                .orElseThrow(()->new ExcecaoUsuarioNaoEncontrado("Usuário Não Encontrado"));
    }
}
