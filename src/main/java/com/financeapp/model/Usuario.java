package com.financeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.financeapp.exception.SaldoInsuficienteException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    private String id;
    private String nome;
    private String email;
    private Carteira carteira;
    private List<Meta> metas = new ArrayList<>();

    public Usuario(String nome, String email) {
        validarNome(nome);
        validarEmail(email);

        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.email = email;
        this.carteira = new Carteira();
    }

    protected Usuario() {
        this.id = null;
        this.carteira = new Carteira();
        this.metas = new ArrayList<>();
    }

    public void adicionarReceita(BigDecimal valor, String descricao, Categoria categoria) {
        Receita receita = new Receita(valor, descricao, categoria);
        carteira.adicionarTransacao(receita);
    }

    public void adicionarDespesa(BigDecimal valor, String descricao, Categoria categoria) {
        BigDecimal saldoAtual = carteira.calcularSaldo();
        if (valor.compareTo(saldoAtual) > 0) {
            throw new SaldoInsuficienteException(saldoAtual, valor);
        }
        Despesa despesa = new Despesa(valor, descricao, categoria);
        carteira.adicionarTransacao(despesa);
    }

    public void adicionarMeta(Meta meta) {
        if (meta == null) {
            throw new IllegalArgumentException("Meta não pode ser nula.");
        }
        boolean jaExiste = metas.stream()
                .anyMatch(m -> m.getCategoria() == meta.getCategoria());
        if (jaExiste) {
            throw new IllegalArgumentException(
                    "Já existe uma meta para " + meta.getCategoria().exibir());
        }
        metas.add(meta);
    }

    public List<Meta> getMetas() {
        return Collections.unmodifiableList(metas);
    }

    public BigDecimal getSaldo() {
        return carteira.calcularSaldo();
    }

    public void exibirResumo() {
        System.out.println("\nUsuário: " + nome + " | " + email);
        carteira.exibirResumo();
    }

    public Carteira getCarteira() { return carteira; }
    public String getId()         { return id; }
    public String getNome()       { return nome; }
    public String getEmail()      { return email; }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
    }
}