package com.financeapp.exception;

public class TransacaoNaoEncontradaException extends RuntimeException {

    private final String id;

    public TransacaoNaoEncontradaException(String id) {
        super("Transação não encontrada com ID: " + id);
        this.id = id;
    }

    public String getId() { return id; }
}