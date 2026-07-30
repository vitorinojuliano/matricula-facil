package com.catijr.backend.controller;

import com.catijr.backend.dto.RespostaDisciplinaCard;
import com.catijr.backend.dto.RespostaDisciplinaDetalhes;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.service.ServicoAutenticacao;
import com.catijr.backend.service.ServicoDisciplina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/MatriculaFacil")
public class    ControladorDisciplina {

    @Autowired
    private ServicoDisciplina servicoDisciplina;

    @Autowired
    private ServicoAutenticacao servicoAutenticacao;

    @GetMapping("/disciplina")
    public ResponseEntity<?> listarDisciplina(Authentication autenticacao) {
        UsuarioModelo usuario = servicoAutenticacao.getUsuarioAutenticado(autenticacao);
        List<RespostaDisciplinaCard> disciplina =  servicoDisciplina.listarTodaDisciplina(usuario.getId());
        return ResponseEntity.ok(disciplina);
    }

    @GetMapping("/disciplina/{id}")
    public ResponseEntity<?> pegarDisciplina(@PathVariable Long id, Authentication  autenticacao) {
        UsuarioModelo usuario = servicoAutenticacao.getUsuarioAutenticado(autenticacao);

        RespostaDisciplinaDetalhes disciplina = servicoDisciplina.findById(id, usuario.getId());
        return ResponseEntity.ok(disciplina);
    }
}
