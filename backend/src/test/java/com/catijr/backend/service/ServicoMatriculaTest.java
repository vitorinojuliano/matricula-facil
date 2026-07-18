package com.catijr.backend.service;

import com.catijr.backend.dto.SolicitacaoMatricula;
import com.catijr.backend.model.DisciplinaModelo;
import com.catijr.backend.model.MatriculaModelo;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.repository.RepositorioDisciplina;
import com.catijr.backend.repository.RepositorioMatricula;
import com.catijr.backend.repository.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicoMatriculaTest {

    @Mock
    private RepositorioMatricula repositorioMatricula;
    @Mock
    private RepositorioUsuario repositorioUsuario;
    @Mock
    private RepositorioDisciplina repositorioDisciplina;

    @InjectMocks
    private ServicoMatricula servicoMatricula;

    private UsuarioModelo usuario;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioModelo();
        usuario.setId(1L);
        usuario.setEmail("aluno@teste.com");
    }

    private void stubUsuarioEncontrado() {
        when(repositorioUsuario.findById(1L)).thenReturn(Optional.of(usuario));
    }

    private DisciplinaModelo criarDisciplina(Long id, String codigo, int creditos, int vagas, String horario,
                                              String status, DisciplinaModelo preRequisito) {
        DisciplinaModelo disciplina = new DisciplinaModelo();
        disciplina.setId(id);
        disciplina.setNome("Disciplina " + codigo);
        disciplina.setCodigo(codigo);
        disciplina.setCreditos(creditos);
        disciplina.setVagas(vagas);
        disciplina.setHorario(horario);
        disciplina.setStatus(status);
        disciplina.setPreRequisito(preRequisito);
        disciplina.setProfessor("Prof. Teste");
        disciplina.setSemestre(1);
        disciplina.setAno(2026);
        return disciplina;
    }

    @Test
    void matricula_deveBloquearReinscricao_quandoMatriculaAnteriorFoiCancelada() {
        stubUsuarioEncontrado();
        // Regra de negócio: cancelar uma matrícula é definitivo — a disciplina não
        // pode voltar a ficar disponível pra inscrição pro mesmo aluno.
        DisciplinaModelo disciplina = criarDisciplina(10L, "AL001", 4, 10, "08:00-10:00", "DISPONIVEL", null);
        when(repositorioDisciplina.findById(10L)).thenReturn(Optional.of(disciplina));
        when(repositorioMatricula.existsByUsuarioIdAndDisciplinaIdAndStatusIn(1L, 10L, List.of("INSCRITO", "CANCELADA"))).thenReturn(true);

        SolicitacaoMatricula requisicao = new SolicitacaoMatricula();
        requisicao.setDisciplinaId(10L);

        assertThatThrownBy(() -> servicoMatricula.matricula(1L, requisicao))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("já se inscreveu");
    }

    @Test
    void matricula_deveLancarExcecao_quandoSemVagas() {
        stubUsuarioEncontrado();
        DisciplinaModelo disciplina = criarDisciplina(10L, "AL001", 4, 0, "08:00-10:00", "DISPONIVEL", null);
        when(repositorioDisciplina.findById(10L)).thenReturn(Optional.of(disciplina));
        when(repositorioMatricula.existsByUsuarioIdAndDisciplinaIdAndStatusIn(1L, 10L, List.of("INSCRITO", "CANCELADA"))).thenReturn(false);

        SolicitacaoMatricula requisicao = new SolicitacaoMatricula();
        requisicao.setDisciplinaId(10L);

        assertThatThrownBy(() -> servicoMatricula.matricula(1L, requisicao))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Sem vagas");
    }

    @Test
    void matricula_deveLancarExcecao_quandoPreRequisitoNaoCumprido() {
        stubUsuarioEncontrado();
        DisciplinaModelo preRequisito = criarDisciplina(1L, "AL001", 4, 10, "08:00-10:00", "DISPONIVEL", null);
        DisciplinaModelo disciplina = criarDisciplina(10L, "ED001", 4, 10, "10:00-12:00", "DISPONIVEL", preRequisito);
        when(repositorioDisciplina.findById(10L)).thenReturn(Optional.of(disciplina));
        when(repositorioMatricula.existsByUsuarioIdAndDisciplinaIdAndStatusIn(1L, 10L, List.of("INSCRITO", "CANCELADA"))).thenReturn(false);
        when(repositorioMatricula.existsByUsuarioIdAndDisciplinaCodigoAndStatus(1L, "AL001", "CONCLUIDA")).thenReturn(false);

        SolicitacaoMatricula requisicao = new SolicitacaoMatricula();
        requisicao.setDisciplinaId(10L);

        assertThatThrownBy(() -> servicoMatricula.matricula(1L, requisicao))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("pre-requisito");
    }

    @Test
    void matricula_deveLancarExcecao_quandoConflitoDeHorario() {
        stubUsuarioEncontrado();
        DisciplinaModelo disciplinaJaInscrita = criarDisciplina(20L, "MD001", 4, 5, "08:00-10:00", "DISPONIVEL", null);
        MatriculaModelo matriculaAtiva = new MatriculaModelo();
        matriculaAtiva.setDisciplina(disciplinaJaInscrita);
        matriculaAtiva.setStatus("INSCRITO");

        DisciplinaModelo disciplina = criarDisciplina(10L, "AL001", 4, 10, "08:00-10:00", "DISPONIVEL", null);
        when(repositorioDisciplina.findById(10L)).thenReturn(Optional.of(disciplina));
        when(repositorioMatricula.existsByUsuarioIdAndDisciplinaIdAndStatusIn(1L, 10L, List.of("INSCRITO", "CANCELADA"))).thenReturn(false);
        when(repositorioMatricula.findByUsuarioIdAndStatus(1L, "INSCRITO")).thenReturn(List.of(matriculaAtiva));

        SolicitacaoMatricula requisicao = new SolicitacaoMatricula();
        requisicao.setDisciplinaId(10L);

        assertThatThrownBy(() -> servicoMatricula.matricula(1L, requisicao))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Conflito de horario");
    }

    @Test
    void matricula_deveLancarExcecao_quandoLimiteDeCreditosExcedido() {
        stubUsuarioEncontrado();
        DisciplinaModelo disciplina = criarDisciplina(10L, "AM001", 6, 10, "10:00-12:00", "DISPONIVEL", null);
        when(repositorioDisciplina.findById(10L)).thenReturn(Optional.of(disciplina));
        when(repositorioMatricula.existsByUsuarioIdAndDisciplinaIdAndStatusIn(1L, 10L, List.of("INSCRITO", "CANCELADA"))).thenReturn(false);
        when(repositorioMatricula.findByUsuarioIdAndStatus(1L, "INSCRITO")).thenReturn(List.of());
        when(repositorioMatricula.somarCreditosPorUsuarioId(1L)).thenReturn(20);

        SolicitacaoMatricula requisicao = new SolicitacaoMatricula();
        requisicao.setDisciplinaId(10L);

        assertThatThrownBy(() -> servicoMatricula.matricula(1L, requisicao))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Limite de creditos");
    }

    @Test
    void cancelarMatricula_deveLiberarVaga_eAtualizarStatusParaCancelada() {
        DisciplinaModelo disciplina = criarDisciplina(10L, "AL001", 4, 5, "08:00-10:00", "DISPONIVEL", null);
        MatriculaModelo matricula = new MatriculaModelo();
        matricula.setId(99L);
        matricula.setUsuario(usuario);
        matricula.setDisciplina(disciplina);
        matricula.setStatus("INSCRITO");

        when(repositorioMatricula.findByIdAndUsuarioId(99L, 1L)).thenReturn(Optional.of(matricula));

        servicoMatricula.cancelarMatricula(99L, 1L);

        assertThat(disciplina.getVagas()).isEqualTo(6);
        assertThat(matricula.getStatus()).isEqualTo("CANCELADA");
        verify(repositorioDisciplina).save(disciplina);
        verify(repositorioMatricula).save(matricula);
    }
}
