package com.catijr.backend.service;

import com.catijr.backend.dto.SolicitacaoAtualizacaoPerfil;
import com.catijr.backend.model.UsuarioModelo;
import com.catijr.backend.repository.RepositorioUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicoUsuarioTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ServicoUsuario servicoUsuario;

    @Test
    void atualizarPerfil_devePermitirManterOProprioEmail() {
        Long usuarioId = 500L;
        UsuarioModelo usuario = new UsuarioModelo();
        usuario.setId(500L);
        usuario.setEmail("aluno@teste.com");
        usuario.setSenha("hash-atual");

        SolicitacaoAtualizacaoPerfil requisicao = new SolicitacaoAtualizacaoPerfil();
        requisicao.setEmail("aluno@teste.com");

        when(repositorioUsuario.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.findByEmail("aluno@teste.com")).thenReturn(Optional.of(usuario));

        servicoUsuario.atualizarPerfil(usuarioId, requisicao);

        verify(repositorioUsuario).save(usuario);
    }
}
