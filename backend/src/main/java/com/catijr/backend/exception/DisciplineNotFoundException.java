package com.catijr.backend.exception;

public class DisciplineNotFoundException extends RuntimeException{
    public DisciplineNotFoundException(String message) {
        super(message);
    }
}
