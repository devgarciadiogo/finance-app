package com.financeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "metas")
public class Meta {

    @Id
    @Column(name = "id")
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "valor_limite", nullable = false)
    private BigDecimal valorLimite;

    public Meta(Categoria categoria, BigDecimal valorLimite) {
        if (categoria == null)
            throw new IllegalArgumentException("Categoria é obrigatória.");
        if (valorLimite == null || valorLimite.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Valor limite deve ser maior que zero.");
        this.id = UUID.randomUUID().toString();
        this.categoria = categoria;
        this.valorLimite = valorLimite;
    }

    protected Meta() {}

    public String getId()              { return id; }
    public Categoria getCategoria()    { return categoria; }
    public BigDecimal getValorLimite() { return valorLimite; }
    public Usuario getUsuario()        { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public void setValorLimite(BigDecimal valorLimite) {
        if (valorLimite == null || valorLimite.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Valor limite deve ser maior que zero.");
        this.valorLimite = valorLimite;
    }

    @Override
    public String toString() {
        return String.format("Meta [%s] → limite R$ %.2f",
                categoria.exibir(), valorLimite);
    }
}