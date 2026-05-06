package com.financeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Despesa extends Transacao {

    public Despesa(BigDecimal valor, String descricao, Categoria categoria) {
        super(valor, descricao, categoria);
    }

    protected Despesa() {}

    @Override
    public String getTipo() {
        return "DESPESA";
    }
}