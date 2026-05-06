package com.financeapp.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Meta {

    private String id;
    private Categoria categoria;
    private BigDecimal valorLimite;

    public Meta(Categoria categoria, BigDecimal valorLimite) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria é Obrigatória.");
        }
        if (valorLimite == null || valorLimite.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor limite deve ser maior que zero.");
        }

        this.id = UUID.randomUUID().toString();
        this.categoria = categoria;
        this.valorLimite = valorLimite;
    }

    protected Meta() {
    }


    public String getId() {
        return id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public BigDecimal getValorLimite() {
        return valorLimite;
    }

    public void setValorLimite(BigDecimal valorLimite) {
        if (valorLimite == null || valorLimite.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor limite deve ser maior que zero.");
        }
        this.valorLimite = valorLimite;
    }

    @Override
    public String toString() {
        return String.format("Meta [%s] → limite R$ %.2f",
                categoria.exibir(), valorLimite);
    }
}
