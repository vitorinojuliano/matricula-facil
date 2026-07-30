package com.catijr.backend.service;

import com.catijr.backend.dto.RespostaDisciplinaDetalhes;
import com.catijr.backend.model.DisciplinaModelo;
import com.catijr.backend.repository.RepositorioDisciplina;
import com.catijr.backend.repository.RepositorioMatricula;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicoDisciplinaTest {

    @Mock
    private RepositorioDisciplina repositorioDisciplina;
    @Mock
    private RepositorioMatricula repositorioMatricula;

    @InjectMocks
    private ServicoDisciplina servicoDisciplina;

    @Test
    void findById_deveMarcarPreRequisitoComoPendente_quandoAlunoNaoConcluiu() {
        DisciplinaModelo preRequisito = new DisciplinaModelo();
        preRequisito.setId(1L);
        preRequisito.setCodigo("ARQ001");
        preRequisito.setNome("Arquitetura de Computadores");

        DisciplinaModelo disciplina = new DisciplinaModelo();
        disciplina.setId(2L);
        disciplina.setCodigo("SO001");
        disciplina.setNome("Sistemas Operacionais");
        disciplina.setCreditos(4);
        disciplina.setVagas(10);
        disciplina.setHorario("18:00-20:00");
        disciplina.setStatus("DISPONIVEL");
        disciplina.setProfessor("Prof. Teste");
        disciplina.setPreRequisito(preRequisito);

        when(repositorioDisciplina.findById(2L)).thenReturn(Optional.of(disciplina));
        when(repositorioMatricula.existsByUsuarioIdAndDisciplinaCodigoAndStatus(1L, "ARQ001", "CONCLUIDA"))
                .thenReturn(false);

        RespostaDisciplinaDetalhes resposta = servicoDisciplina.findById(2L, 1L);

        assertThat(resposta.getPreRequisitoStatus()).isEqualTo("FALTA: ARQ001");
        assertThat(resposta.getPreRequisitosDetalhados().get(0).getStatus()).isEqualTo("PENDENTE");
    }
}
