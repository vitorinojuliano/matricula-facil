package com.catijr.backend.controller;

import com.catijr.backend.dto.RespostaMatricula;
import com.catijr.backend.dto.SolicitacaoMatricula;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.service.ServicoAutenticacao;
import com.catijr.backend.service.ServicoMatricula;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/MatriculaFacil")
public class ControladorMatricula {
    @Autowired
    private ServicoMatricula servicoMatricula;

    @Autowired
    private ServicoAutenticacao servicoAutenticacao;

    @PostMapping("/matricula")
    public ResponseEntity<?> matricula(@Valid @RequestBody SolicitacaoMatricula requisicao, Authentication autenticacao) {
        UsuarioModelo usuario = servicoAutenticacao.getUsuarioAutenticado(autenticacao);

        RespostaMatricula matricula = servicoMatricula.matricula(usuario.getId(), requisicao);

        return ResponseEntity.ok(Map.of(
                "message", "Matricula registrado com sucesso",
                "matricula", matricula
        ));
    }

    @GetMapping("/matricula")
    public ResponseEntity<?> listarMatriculas(Authentication autenticacao,
                                              @RequestParam(required = false) Integer semestre,
                                              @RequestParam(required = false) Integer ano) {
        UsuarioModelo usuario = servicoAutenticacao.getUsuarioAutenticado(autenticacao);

        List<RespostaMatricula> matriculas = servicoMatricula.listarMatriculasPorFiltro(usuario.getId(), semestre, ano);

        Integer creditos = servicoMatricula.calcularCreditosAtuais(usuario.getId(), semestre, ano);

        return ResponseEntity.ok(Map.of(
                "matriculas", matriculas,
                "creditosAtuais", creditos
        ));
    }

    @DeleteMapping("/matriculas/{id}")
    public ResponseEntity<?> cancelarMatricula(@PathVariable Long id, Authentication autenticacao) {
        UsuarioModelo usuario = servicoAutenticacao.getUsuarioAutenticado(autenticacao);

        servicoMatricula.cancelarMatricula(id, usuario.getId());
        return ResponseEntity.ok(Map.of(
                "message", "Matrícula cancelada com sucesso"
        ));
    }

}
