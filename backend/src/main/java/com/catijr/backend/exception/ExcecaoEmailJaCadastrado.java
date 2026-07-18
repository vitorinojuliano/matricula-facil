package com.catijr.backend.exception;

public class ExcecaoEmailJaCadastrado extends RuntimeException {
    public ExcecaoEmailJaCadastrado(String message) {
        super(message);
    }
}
