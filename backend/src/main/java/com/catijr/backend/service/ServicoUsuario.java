package com.catijr.backend.service;

import com.catijr.backend.dto.SolicitacaoAtualizacaoPerfil;
import com.catijr.backend.dto.SolicitacaoLogin;
import com.catijr.backend.dto.SolicitacaoCadastro;
import com.catijr.backend.exception.ExcecaoEmailJaCadastrado;
import com.catijr.backend.exception.ExcecaoRegraDeNegocio;
import com.catijr.backend.exception.ExcecaoSenhaIncompativel;
import com.catijr.backend.exception.ExcecaoUsuarioNaoEncontrado;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.repository.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServicoUsuario {
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioModelo cadastrarUsuario(SolicitacaoCadastro requisicao) {

        if(!requisicao.getSenha().equals(requisicao.getConfirmarSenha())) {
            throw new ExcecaoSenhaIncompativel("Senhas diferentes");
        }

        if(repositorioUsuario.findByEmail(requisicao.getEmail()).isPresent()){
            throw new ExcecaoEmailJaCadastrado("Email já cadastrado");
        }

        UsuarioModelo usuario = new UsuarioModelo();
        usuario.setEmail(requisicao.getEmail());
        usuario.setSenha(passwordEncoder.encode(requisicao.getSenha()));

        return repositorioUsuario.save(usuario);
    }

    public UsuarioModelo loginUser(SolicitacaoLogin request){
        UsuarioModelo usuario = repositorioUsuario.findByEmail(request.getEmail()).orElseThrow(()-> new ExcecaoUsuarioNaoEncontrado("Email ou senha inválidos."));
        if(!passwordEncoder.matches(request.getSenha(), (usuario.getSenha()))) {
            throw new ExcecaoUsuarioNaoEncontrado("Email ou senha inválidos.");
        }
        return usuario;

    }

    public void atualizarPerfil(Long usuarioId, SolicitacaoAtualizacaoPerfil solicitacao){
        UsuarioModelo usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(()-> new ExcecaoUsuarioNaoEncontrado("Usuário não encontrado"));
        if(solicitacao.getNovaSenha() != null && !solicitacao.getNovaSenha().isEmpty()) {
            if (solicitacao.getSenhaAtual() == null || solicitacao.getSenhaAtual().isEmpty()) {
                throw new ExcecaoRegraDeNegocio("Senha atual é obrigatória para alterar a senha");
            }

            if (!passwordEncoder.matches(solicitacao.getSenhaAtual(), (usuario.getSenha()))) {
                throw new ExcecaoSenhaIncompativel("Senhas incorreta");
            }

            usuario.setSenha(passwordEncoder.encode(solicitacao.getNovaSenha()));
        }

        if(solicitacao.getEmail() != null && !solicitacao.getEmail().isEmpty()) {
            repositorioUsuario.findByEmail(solicitacao.getEmail())
                    .ifPresent(u -> {
                        if (!u.getId().equals(usuarioId)){
                            throw new ExcecaoRegraDeNegocio("Email já está em uso");
                        }
            });

            usuario.setEmail(solicitacao.getEmail());
        }

        repositorioUsuario.save(usuario);
    }
}
