package com.financeapp.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("RECEITA")
public class Receita extends Transacao {

    public Receita(BigDecimal valor, String descricao, Categoria categoria) {
        super(valor, descricao, categoria);
    }

    protected Receita() {}

    @Override
    public String getTipo() { return "RECEITA"; }
}