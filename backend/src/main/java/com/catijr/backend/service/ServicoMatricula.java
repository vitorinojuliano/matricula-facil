package com.catijr.backend.service;

import com.catijr.backend.dto.SolicitacaoMatricula;
import com.catijr.backend.dto.RespostaMatricula;
import com.catijr.backend.exception.ExcecaoDisciplinaNaoEncontrada;
import com.catijr.backend.exception.ExcecaoMatriculaNaoEncontrada;
import com.catijr.backend.exception.ExcecaoRegraDeNegocio;
import com.catijr.backend.exception.ExcecaoUsuarioNaoEncontrado;
import com.catijr.backend.model.DisciplinaModelo;
import com.catijr.backend.model.MatriculaModelo;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.repository.RepositorioDisciplina;
import com.catijr.backend.repository.RepositorioMatricula;
import com.catijr.backend.repository.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoMatricula {

    @Autowired
    private RepositorioMatricula repositorioMatricula;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioDisciplina repositorioDisciplina;

    @Transactional
    public RespostaMatricula matricula(Long usuarioId, SolicitacaoMatricula requisicao) {
        UsuarioModelo usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(()->new ExcecaoUsuarioNaoEncontrado("Usuário não encontrado"));

        DisciplinaModelo disciplina = repositorioDisciplina.findById(requisicao.getDisciplinaId())
                .orElseThrow(()-> new ExcecaoDisciplinaNaoEncontrada("Disciplina não encontrada"));

        if(repositorioMatricula.existsByUsuarioIdAndDisciplinaIdAndStatusIn(usuarioId, disciplina.getId(), List.of("INSCRITO", "CANCELADA"))) {
            throw new ExcecaoRegraDeNegocio("Você já se inscreveu nesta disciplina anteriormente");
        }
        if(disciplina.getVagas() <= 0){
            throw new ExcecaoRegraDeNegocio("Sem vagas");
        }
        if(disciplina.getPreRequisito() != null){
            boolean temPreRequisito = repositorioMatricula.existsByUsuarioIdAndDisciplinaCodigoAndStatus(usuarioId,
                    disciplina.getPreRequisito().getCodigo(), "CONCLUIDA");

            if(!temPreRequisito){
                throw new ExcecaoRegraDeNegocio("Sem pre-requisito de " + disciplina.getPreRequisito().getCodigo());
            }
        }

        if(!disciplina.getStatus().equals("DISPONIVEL")){
            throw new ExcecaoRegraDeNegocio("Disciplina não está disponivel para matricula");
        }

        List<MatriculaModelo> matriculasAtivas = repositorioMatricula.findByUsuarioIdAndStatus(usuarioId, "INSCRITO");
        for (MatriculaModelo m :  matriculasAtivas) {
            boolean mesmoPeriodo = m.getDisciplina().getSemestre().equals(disciplina.getSemestre())
                    && m.getDisciplina().getAno().equals(disciplina.getAno());
            if (mesmoPeriodo && m.getDisciplina().getHorario().equals(disciplina.getHorario())) {
                throw new ExcecaoRegraDeNegocio("Conflito de horario com disciplina: "+m.getDisciplina().getHorario());
            }
        }

        Integer creditosAtuais = calcularCreditosAtuais(usuarioId, disciplina.getSemestre(), disciplina.getAno());
        if(creditosAtuais + disciplina.getCreditos()>24){
            throw new ExcecaoRegraDeNegocio("Limite de creditos atingido");
        }


        MatriculaModelo matricula = new MatriculaModelo();
        matricula.setUsuario(usuario);
        matricula.setDisciplina(disciplina);
        matricula.setDataMatricula(LocalDateTime.now());
        matricula.setStatus("INSCRITO");

        disciplina.setVagas(disciplina.getVagas()-1);
        repositorioDisciplina.save(disciplina);

        MatriculaModelo matriculaSalva = repositorioMatricula.save(matricula);

        return paraResposta(matriculaSalva);
    }

    public Integer calcularCreditosAtuais(Long usuarioId, Integer semestre, Integer ano){
        Integer creditos = repositorioMatricula.somarCreditosPorUsuarioId(usuarioId, semestre, ano);
        return creditos == null ? 0 : creditos;
    }

    private RespostaMatricula paraResposta(MatriculaModelo matricula) {
        RespostaMatricula resposta = new RespostaMatricula();

        resposta.setId(matricula.getId());
        resposta.setNomeDaMatricula(matricula.getDisciplina().getNome());
        resposta.setCodigoDaDisciplina(matricula.getDisciplina().getCodigo());
        resposta.setCreditos(matricula.getDisciplina().getCreditos());
        resposta.setHorario(matricula.getDisciplina().getHorario());
        resposta.setStatus(matricula.getStatus());
        resposta.setDataMatricula(matricula.getDataMatricula());
        resposta.setSemestre(matricula.getDisciplina().getSemestre());
        resposta.setAno(matricula.getDisciplina().getAno());

        return resposta;
    }
    @Transactional
    public void cancelarMatricula(long matriculaId, long usuarioId){
        MatriculaModelo matricula = repositorioMatricula
                .findByIdAndUsuarioId(matriculaId, usuarioId)
                .orElseThrow(()-> new ExcecaoMatriculaNaoEncontrada("Matricula não encontrada"));
        if(!matricula.getStatus().equals("INSCRITO")){
            throw new ExcecaoRegraDeNegocio("Não pode ser cancelado");
        }

        DisciplinaModelo disciplina = matricula.getDisciplina();
        disciplina.setVagas(disciplina.getVagas()+1);
        repositorioDisciplina.save(disciplina);

        matricula.setStatus("CANCELADA");
        repositorioMatricula.save(matricula);
    }

    public List<RespostaMatricula> listarMatriculasPorFiltro(Long usuarioId, Integer semestre, Integer ano) {
        List<MatriculaModelo> matriculas = repositorioMatricula.findMatriculaByFilter(usuarioId, semestre, ano);
        return matriculas.stream()
                .map(this::paraResposta)
                .collect(Collectors.toList());
    }
}
