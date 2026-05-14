package com.financeapp.dto;

import com.financeapp.model.Categoria;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AdicionarMetaRequest(
        @NotNull(message = "Categoria é obrigatória")
        Categoria categoria,

        @NotNull(message = "Valor limite é obrigatório")
        @Positive(message = "Valor limite deve ser maior que zero")
        BigDecimal valorLimite
) {}