package com.catijr.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManipuladorGlobalDeExcecoes {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarDadosInvalidos(MethodArgumentNotValidException e) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError erro : e.getBindingResult().getFieldErrors()) {
            erros.put(erro.getField(), erro.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "VALIDATION_ERROR", "fields", erros));
    }

    @ExceptionHandler(ExcecaoUsuarioNaoEncontrado.class)
    public ResponseEntity<?> tratarUsuarioNaoEncontrado(ExcecaoUsuarioNaoEncontrado e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "USER_NOT_FOUND", "message", e.getMessage()));
    }

    @ExceptionHandler(ExcecaoEmailJaCadastrado.class)
    public ResponseEntity<?> tratarEmailJaCadastrado(ExcecaoEmailJaCadastrado e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "EMAIL_ALREADY", "message", e.getMessage()));
    }

    @ExceptionHandler(ExcecaoSenhaIncompativel.class)
    public ResponseEntity<?> tratarSenhaIncompativel(ExcecaoSenhaIncompativel e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "PASSWORD_MISMATCH", "message", e.getMessage()));
    }

    @ExceptionHandler(ExcecaoDisciplinaNaoEncontrada.class)
    public ResponseEntity<?> tratarDisciplinaNaoEncontrada(ExcecaoDisciplinaNaoEncontrada e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "COURSE_NOT_FOUND", "message", e.getMessage()));
    }

    @ExceptionHandler(ExcecaoMatriculaNaoEncontrada.class)
    public ResponseEntity<?> tratarMatriculaNaoEncontrada(ExcecaoMatriculaNaoEncontrada e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "ENROLLMENT_NOT_FOUND", "message", e.getMessage()));
    }

    @ExceptionHandler(ExcecaoRegraDeNegocio.class)
    public ResponseEntity<?> tratarRegraDeNegocio(ExcecaoRegraDeNegocio e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "BUSINESS_RULE_ERROR", "message", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> tratarErroInesperado(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "INTERNAL_SERVER_ERROR", "message", "Erro interno no servidor"));
    }
}
