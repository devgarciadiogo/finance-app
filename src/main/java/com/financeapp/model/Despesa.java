package com.financeapp.model;

import java.math.BigDecimal;

public class Despesa extends Transacao {

    public Despesa(BigDecimal valor, String descricao, Categoria categoria) {
        super(valor, descricao, categoria);
    }

    @Override
    public String getTipo() {
        return "DESPESA";
    }
}