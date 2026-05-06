package com.financeapp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Receita.class, name = "RECEITA"),
        @JsonSubTypes.Type(value = Despesa.class, name = "DESPESA")
})

public abstract class Transacao {

    private String id;
    private LocalDate data;
    private BigDecimal valor;
    private String descricao;
    private Categoria categoria;

    public Transacao(BigDecimal valor, String descricao, Categoria categoria) {
        // Validações
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }

        this.id = UUID.randomUUID().toString();
        this.data = LocalDate.now();
        this.valor = valor;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    protected Transacao() {
        this.id = null;
        this.data = null;
        this.valor = null;
        this.descricao = null;
        this.categoria = null;
    }

    // Método abstrato — cada subclasse OBRIGA a dizer o seu tipo
    public abstract String getTipo();

    // Getters
    public String getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    // Setter com validação — só o valor pode mudar depois
    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        this.valor = valor;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | R$ %.2f | %s | %s",
                getTipo(),
                descricao,
                valor,
                categoria.exibir(),
                data);
    }
}