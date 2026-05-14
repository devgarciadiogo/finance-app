package com.financeapp.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("DESPESA")
public class Despesa extends Transacao {

    public Despesa(BigDecimal valor, String descricao, Categoria categoria) {
        super(valor, descricao, categoria);
    }

    protected Despesa() {}

    @Override
    public String getTipo() { return "DESPESA"; }
}