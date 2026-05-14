package com.financeapp.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacoes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo", visible = false)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Receita.class, name = "RECEITA"),
        @JsonSubTypes.Type(value = Despesa.class, name = "DESPESA")
})
public abstract class Transacao {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private Categoria categoria;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Transacao(BigDecimal valor, String descricao, Categoria categoria) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }
        this.id = java.util.UUID.randomUUID().toString();
        this.data = LocalDate.now();
        this.valor = valor;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    protected Transacao() {}

    @JsonIgnore
    public abstract String getTipo();

    public String getId()           { return id; }
    public LocalDate getData()      { return data; }
    public BigDecimal getValor()    { return valor; }
    public String getDescricao()    { return descricao; }
    public Categoria getCategoria() { return categoria; }
    public Usuario getUsuario()     { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        this.valor = valor;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | R$ %.2f | %s | %s",
                getTipo(), descricao, valor, categoria.exibir(), data);
    }
}