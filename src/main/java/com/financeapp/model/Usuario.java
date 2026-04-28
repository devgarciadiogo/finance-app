package com.financeapp.model;

import com.financeapp.exception.SaldoInsuficienteException;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Usuario {

    private final String id;
    private String nome;
    private String email;
    private final Carteira carteira;
    private final List<Meta> metas = new ArrayList<>();

    public Usuario(String nome, String email) {
        validarNome(nome);
        validarEmail(email);

        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.email = email;
        this.carteira = new Carteira();
    }

    public void adicionarReceita(BigDecimal valor, String descricao, Categoria categoria) {
        Receita receita = new Receita(valor, descricao, categoria);
        carteira.adicionarTransacao(receita);
    }

    public void adicionarDespesa(BigDecimal valor, String descricao, Categoria categoria) {
        // Aqui usamos nossa exceção customizada!
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
        // Não permite duas metas pra mesma categoria
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

    // Métodos privados de validação — só a classe conhece essas regras
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