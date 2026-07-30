package com.catijr.backend.service;

import com.catijr.backend.dto.PreRequisitoDTO;
import com.catijr.backend.dto.RespostaDisciplinaCard;
import com.catijr.backend.dto.RespostaDisciplinaDetalhes;
import com.catijr.backend.exception.ExcecaoDisciplinaNaoEncontrada;
import com.catijr.backend.model.DisciplinaModelo;
import com.catijr.backend.repository.RepositorioDisciplina;
import com.catijr.backend.repository.RepositorioMatricula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoDisciplina {
    @Autowired
    private RepositorioDisciplina repositorioDisciplina;

    @Autowired
    private RepositorioMatricula repositorioMatricula;

    private void preencherCamposBase(RespostaDisciplinaCard resposta, DisciplinaModelo disciplina) {
        resposta.setId(disciplina.getId());
        resposta.setNome(disciplina.getNome());
        resposta.setCodigo(disciplina.getCodigo());
        resposta.setCreditos(disciplina.getCreditos());
        resposta.setVagas(disciplina.getVagas());
        resposta.setHorario(disciplina.getHorario());
        resposta.setStatus(disciplina.getStatus());
        resposta.setSemestre(disciplina.getSemestre());
        resposta.setAno(disciplina.getAno());
        resposta.setPreRequisitoCodigo(
                disciplina.getPreRequisito() != null ? disciplina.getPreRequisito().getCodigo() : null
        );
    }

    private RespostaDisciplinaCard paraRespostaCartao(DisciplinaModelo disciplina, Long usuarioId) {
        RespostaDisciplinaCard resposta = new RespostaDisciplinaCard();
        preencherCamposBase(resposta, disciplina);
        preencherStatusMatricula(resposta, disciplina, usuarioId);
        return resposta;
    }

    private void preencherStatusMatricula(RespostaDisciplinaCard resposta, DisciplinaModelo disciplina, Long usuarioId) {
        repositorioMatricula.findFirstByUsuarioIdAndDisciplinaIdOrderByIdDesc(usuarioId, disciplina.getId())
                .ifPresent(matricula -> {
                    resposta.setMatriculaId(matricula.getId());
                    resposta.setMatriculaStatus(matricula.getStatus());
                });
    }

    public List<RespostaDisciplinaCard> listarTodaDisciplina(Long usuarioId) {
        return repositorioDisciplina.findAll().stream()
                .map(disciplina -> {
                    RespostaDisciplinaCard resposta = paraRespostaCartao(disciplina, usuarioId);
                    String status = calcularStatusPreRequisito(disciplina, usuarioId);
                    resposta.setPreRequisitoStatus(status);
                    return resposta;
                })
                .collect(Collectors.toList());
    }
    private RespostaDisciplinaDetalhes paraRespostaDetalhada(DisciplinaModelo disciplina, Long usuarioId) {
        RespostaDisciplinaDetalhes resposta = new RespostaDisciplinaDetalhes();
        preencherCamposBase(resposta, disciplina);
        preencherStatusMatricula(resposta, disciplina, usuarioId);

        resposta.setProfessor(disciplina.getProfessor());
        resposta.setDescricao(disciplina.getDescricao());
        resposta.setPreRequisitosDetalhados(montarListaPreRequisito(disciplina, usuarioId));

        return resposta;
    }
    public RespostaDisciplinaDetalhes findById(Long id, Long usuarioId) {
        DisciplinaModelo disciplina = repositorioDisciplina.findById(id)
                .orElseThrow(() -> new ExcecaoDisciplinaNaoEncontrada("Disciplina não encontrada"));

        RespostaDisciplinaDetalhes resposta = paraRespostaDetalhada(disciplina, usuarioId);

        String status = calcularStatusPreRequisito(disciplina, usuarioId);
        resposta.setPreRequisitoStatus(status);

        return resposta;
    }

    private boolean preRequisitoCumprido(DisciplinaModelo preRequisito, Long usuarioId) {
        return repositorioMatricula
                .existsByUsuarioIdAndDisciplinaCodigoAndStatus(usuarioId, preRequisito.getCodigo(), "CONCLUIDA");
    }

    private String calcularStatusPreRequisito(DisciplinaModelo disciplina, Long usuarioId) {
        DisciplinaModelo preRequisito = disciplina.getPreRequisito();
        if(preRequisito == null){
            return null;
        }
        return preRequisitoCumprido(preRequisito, usuarioId) ? "ATENDIDO" : "FALTA: " + preRequisito.getCodigo();
    }

    private List<PreRequisitoDTO> montarListaPreRequisito(DisciplinaModelo disciplina, Long usuarioId) {
        List<PreRequisitoDTO> lista = new ArrayList<>();
        DisciplinaModelo preRequisito = disciplina.getPreRequisito();

        if(preRequisito != null){
            PreRequisitoDTO detalhe = new PreRequisitoDTO();
            detalhe.setCodigo(preRequisito.getCodigo());
            detalhe.setNome(preRequisito.getNome());
            detalhe.setStatus(preRequisitoCumprido(preRequisito, usuarioId) ? "CURSADO" : "PENDENTE");
            lista.add(detalhe);
        }
        return lista;
    }
}
