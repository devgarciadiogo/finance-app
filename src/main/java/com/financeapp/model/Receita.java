package com.financeapp.model;

import java.math.BigDecimal;

public class Receita extends Transacao {

    public Receita(BigDecimal valor, String descricao, Categoria categoria) {
        super(valor, descricao, categoria);
    }

    @Override
    public String getTipo() {
        return "RECEITA";
    }
}