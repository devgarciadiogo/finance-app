package com.financeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Receita extends Transacao {

    public Receita(BigDecimal valor, String descricao, Categoria categoria) {
        super(valor, descricao, categoria);
    }

    protected Receita() {}

    @Override
    public String getTipo() {
        return "RECEITA";
    }
}