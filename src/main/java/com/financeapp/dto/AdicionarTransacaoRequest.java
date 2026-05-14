package com.financeapp.dto;

import com.financeapp.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AdicionarTransacaoRequest(
        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser maior que zero")
        BigDecimal valor,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Categoria é obrigatória")
        Categoria categoria
) {}