package com.financeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Erros de validação do @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                erros.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(erros);
    }

    // Regras de negócio violadas
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("erro", ex.getMessage()));
    }

    // Saldo insuficiente
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Map<String, String>> handleSaldoInsuficiente(
            SaldoInsuficienteException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("erro", ex.getMessage()));
    }

    // Transação não encontrada
    @ExceptionHandler(TransacaoNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleNaoEncontrado(
            TransacaoNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", ex.getMessage()));
    }
}