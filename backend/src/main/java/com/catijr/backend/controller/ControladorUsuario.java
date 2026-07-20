package com.catijr.backend.controller;

import com.catijr.backend.service.ServicoJwt;
import com.catijr.backend.dto.RespostaPerfil;
import com.catijr.backend.dto.SolicitacaoAtualizacaoPerfil;
import com.catijr.backend.dto.SolicitacaoLogin;
import com.catijr.backend.dto.SolicitacaoCadastro;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.service.ServicoAutenticacao;
import com.catijr.backend.service.ServicoUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/MatriculaFacil")
public class ControladorUsuario {
    @Autowired
    private ServicoUsuario servicoUsuario;

    @Autowired
    private ServicoJwt servicoJwt;

    @Autowired
    private ServicoAutenticacao servicoAutenticacao;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@Valid @RequestBody SolicitacaoCadastro requisicao){
        UsuarioModelo usuario = servicoUsuario.cadastrarUsuario(requisicao);
        return ResponseEntity.ok("Usuário criado com ID: " + usuario.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SolicitacaoLogin requisicao){
        UsuarioModelo usuario = servicoUsuario.loginUser(requisicao);

        String token = servicoJwt.gerarToken(usuario.getEmail());

        return ResponseEntity.ok(Map.of("token", token,
                "user", Map.of("id", usuario.getId(), "email", usuario.getEmail())));
    }

    @GetMapping("/user/me")
    public ResponseEntity<?> pegarPerfil(Authentication autenticacao){
        UsuarioModelo usuario = servicoAutenticacao.getUsuarioAutenticado(autenticacao);

        RespostaPerfil resposta = new RespostaPerfil();
        resposta.setId(usuario.getId());
        resposta.setEmail(usuario.getEmail());

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/user/me")
    public ResponseEntity<?> atualizarPerfil(@RequestBody SolicitacaoAtualizacaoPerfil requisicao, Authentication autenticacao){
        UsuarioModelo usuario = servicoAutenticacao.getUsuarioAutenticado(autenticacao);

        servicoUsuario.atualizarPerfil(usuario.getId(), requisicao);
        return ResponseEntity.ok(Map.of("message", "Perfil atualizado com Sucesso"));
    }

}
