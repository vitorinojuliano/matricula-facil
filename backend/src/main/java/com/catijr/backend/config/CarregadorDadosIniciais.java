package com.catijr.backend.config;

import com.catijr.backend.model.DisciplinaModelo;
import com.catijr.backend.model.MatriculaModelo;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.repository.RepositorioDisciplina;
import com.catijr.backend.repository.RepositorioMatricula;
import com.catijr.backend.repository.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CarregadorDadosIniciais implements CommandLineRunner {

    @Autowired
    private RepositorioDisciplina repositorioDisciplina;

    @Autowired
    private RepositorioMatricula repositorioMatricula;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (repositorioDisciplina.count() > 0) {
            return;
        }

        Map<String, DisciplinaModelo> disciplinas = new HashMap<>();

        disciplinas.put("AL001", salvarDisciplina("Algoritmos", "AL001", 4, 40, "10:00-12:00",
                "CONCLUIDA", "Prof. Marcelo Pereira",
                "Fundamentos de algoritmos, lógica de programação e resolução de problemas computacionais.",
                null, 1, 2025));

        disciplinas.put("ED001", salvarDisciplina("Estrutura de Dados", "ED001", 4, 30, "08:00-10:00",
                "CONCLUIDA", "Prof. Carlos Silva",
                "Estudo das estruturas de dados lineares e não lineares, análise de algoritmos e organização eficiente de dados.",
                disciplinas.get("AL001"), 2, 2025));

        disciplinas.put("BD001", salvarDisciplina("Banco de Dados", "BD001", 4, 25, "14:00-16:00",
                "DISPONIVEL", "Prof. Ricardo Oliveira",
                "Modelagem de bancos de dados relacionais e desenvolvimento de consultas em SQL.",
                disciplinas.get("ED001"), 2, 2026));

        disciplinas.put("ES001", salvarDisciplina("Engenharia de Software", "ES001", 4, 20, "16:00-18:00",
                "DISPONIVEL", "Profa. Ana Santos",
                "Introdução ao desenvolvimento de software, boas práticas e metodologias de projeto.",
                disciplinas.get("ED001"), 2, 2026));

        disciplinas.put("MD001", salvarDisciplina("Matemática Discreta", "MD001", 4, 31, "08:00-12:00",
                "INDISPONIVEL", "Profa. Juliana Costa",
                "Conceitos de lógica matemática, conjuntos, relações, grafos e combinatória.",
                null, 2, 2026));

        disciplinas.put("POO001", salvarDisciplina("Programação Orientada a Objetos", "POO001", 4, 35, "10:00-12:00",
                "CONCLUIDA", "Prof. João Lima",
                "Fundamentos da Programação Orientada a Objetos.",
                null, 2, 2025));

        disciplinas.put("CAL001", salvarDisciplina("Cálculo I", "CAL001", 4, 40, "14:00-16:00",
                "CONCLUIDA", "Profa. Fernanda Souza",
                "Estudo de limites, derivadas e integrais.",
                null, 1, 2025));

        disciplinas.put("ARQ001", salvarDisciplina("Arquitetura de Computadores", "ARQ001", 2, 20, "18:00-20:00",
                "CONCLUIDA", "Prof. Ricardo Mendes",
                "Introdução à arquitetura de computadores.",
                null, 1, 2025));

        disciplinas.put("EXT001", salvarDisciplina("Projeto de Extensão", "EXT001", 6, 15, "19:00-22:00",
                "INDISPONIVEL", "Profa. Mariana Costa",
                "Projeto extensionista interdisciplinar.",
                null, 2, 2026));

        disciplinas.put("OPT001", salvarDisciplina("Tópicos Especiais", "OPT001", 8, 0, "20:00-22:00",
                "DISPONIVEL", "Prof. Bruno Alves",
                "Tópicos Especiais em Computação.",
                null, 2, 2026));

        disciplinas.put("SO001", salvarDisciplina("Sistemas Operacionais", "SO001", 4, 30, "18:00-20:00",
                "CONCLUIDA", "Prof. Diego Martins",
                "Gerenciamento de processos, memória, sistemas de arquivos e concorrência.",
                disciplinas.get("ARQ001"), 2, 2025));

        disciplinas.put("WEB001", salvarDisciplina("Programação Web", "WEB001", 4, 25, "18:00-20:00",
                "DISPONIVEL", "Profa. Marina Costa",
                "Desenvolvimento de aplicações web com HTML, CSS, JavaScript e APIs.",
                disciplinas.get("POO001"), 2, 2026));

        disciplinas.put("CAL002", salvarDisciplina("Cálculo II", "CAL002", 4, 30, "16:00-18:00",
                "CONCLUIDA", "Prof. Lucas Ribeiro",
                "Integrais múltiplas, séries e aplicações.",
                disciplinas.get("CAL001"), 2, 2025));

        disciplinas.put("IA001", salvarDisciplina("Inteligência Artificial", "IA001", 4, 20, "08:00-10:00",
                "DISPONIVEL", "Prof. Rafael Gomes",
                "Busca, representação do conhecimento e técnicas de inteligência artificial.",
                disciplinas.get("BD001"), 2, 2026));

        disciplinas.put("AM001", salvarDisciplina("Aprendizado de Máquina", "AM001", 6, 20, "10:00-12:00",
                "INDISPONIVEL", "Profa. Camila Alves",
                "Modelos supervisionados e não supervisionados de aprendizado de máquina.",
                disciplinas.get("IA001"), 2, 2026));

        disciplinas.put("PI001", salvarDisciplina("Projeto Integrador", "PI001", 8, 10, "16:00-18:00",
                "INDISPONIVEL", "Prof. Eduardo Silva",
                "Projeto interdisciplinar envolvendo engenharia de software.",
                disciplinas.get("ES001"), 2, 2026));

        disciplinas.put("RC001", salvarDisciplina("Redes de Computadores", "RC001", 4, 20, "12:00-14:00",
                "DISPONIVEL", "Prof. Henrique Barros",
                "Fundamentos de redes, protocolos de comunicação e arquitetura TCP/IP.",
                null, 2, 2026));

        disciplinas.put("COMP001", salvarDisciplina("Compiladores", "COMP001", 4, 15, "06:00-08:00",
                "DISPONIVEL", "Profa. Beatriz Nogueira",
                "Análise léxica, sintática e semântica; construção de compiladores.",
                disciplinas.get("ED001"), 2, 2026));

        // Mesmo horario de BD001 (14:00-16:00), que o aluno seed ja tem INSCRITO --
        // serve pra demonstrar o bloqueio por conflito de horario na tela de catalogo.
        disciplinas.put("SEG001", salvarDisciplina("Segurança da Informação", "SEG001", 4, 10, "14:00-16:00",
                "DISPONIVEL", "Prof. Rodrigo Farias",
                "Criptografia, autenticação e boas práticas de segurança de sistemas.",
                null, 2, 2026));

        UsuarioModelo aluno = new UsuarioModelo();
        aluno.setEmail("aluno@matriculafacil.com");
        aluno.setSenha(passwordEncoder.encode("senha123"));
        aluno = repositorioUsuario.save(aluno);

        salvarMatricula(aluno, disciplinas.get("AL001"), "CONCLUIDA");
        salvarMatricula(aluno, disciplinas.get("ED001"), "CONCLUIDA");
        salvarMatricula(aluno, disciplinas.get("POO001"), "CONCLUIDA");
        salvarMatricula(aluno, disciplinas.get("CAL001"), "CONCLUIDA");
        salvarMatricula(aluno, disciplinas.get("CAL002"), "CONCLUIDA");
        salvarMatricula(aluno, disciplinas.get("ARQ001"), "REPROVADA");
        salvarMatricula(aluno, disciplinas.get("SO001"), "REPROVADA");
        salvarMatricula(aluno, disciplinas.get("EXT001"), "CANCELADA");
        salvarMatricula(aluno, disciplinas.get("MD001"), "INSCRITO");
        salvarMatricula(aluno, disciplinas.get("WEB001"), "INSCRITO");
        salvarMatricula(aluno, disciplinas.get("BD001"), "INSCRITO");
        salvarMatricula(aluno, disciplinas.get("ES001"), "INSCRITO");
    }

    private DisciplinaModelo salvarDisciplina(String nome, String codigo, int creditos, int vagas, String horario,
                                               String status, String professor, String descricao,
                                               DisciplinaModelo preRequisito, int semestre, int ano) {
        DisciplinaModelo disciplina = new DisciplinaModelo();
        disciplina.setNome(nome);
        disciplina.setCodigo(codigo);
        disciplina.setCreditos(creditos);
        disciplina.setVagas(vagas);
        disciplina.setHorario(horario);
        disciplina.setStatus(status);
        disciplina.setProfessor(professor);
        disciplina.setDescricao(descricao);
        disciplina.setPreRequisito(preRequisito);
        disciplina.setSemestre(semestre);
        disciplina.setAno(ano);
        return repositorioDisciplina.save(disciplina);
    }

    private void salvarMatricula(UsuarioModelo usuario, DisciplinaModelo disciplina, String status) {
        MatriculaModelo matricula = new MatriculaModelo();
        matricula.setUsuario(usuario);
        matricula.setDisciplina(disciplina);
        matricula.setStatus(status);
        matricula.setDataMatricula(LocalDateTime.now());
        repositorioMatricula.save(matricula);
    }
}
