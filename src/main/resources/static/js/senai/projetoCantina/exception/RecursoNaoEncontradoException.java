package com.senai.projetoCantina.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com ID " + id + " não encontrado(a).");
    }
}