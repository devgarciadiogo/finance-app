package com.financeapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carteira {

    private final List<Transacao> transacoes;

    public Carteira() {
        this.transacoes = new ArrayList<>();
    }

    public void adicionarTransacao(Transacao transacao) {
        if (transacao == null) {
            throw new IllegalArgumentException("Transação não pode ser nula.");
        }
        transacoes.add(transacao);
    }

    public BigDecimal calcularSaldo() {
        BigDecimal receitas = transacoes.stream()
                .filter(t -> t instanceof Receita)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal despesas = transacoes.stream()
                .filter(t -> t instanceof Despesa)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return receitas.subtract(despesas);
    }

    public BigDecimal totalReceitas() {
        return transacoes.stream()
                .filter(t -> t instanceof Receita)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalDespesas() {
        return transacoes.stream()
                .filter(t -> t instanceof Despesa)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Transacao> getTransacoes() {
        // Retorna cópia quem está de fora não consegue alterar
        return Collections.unmodifiableList(transacoes);
    }

    public List<Transacao> filtrarPorCategoria(Categoria categoria) {
        return transacoes.stream()
                .filter(t -> t.getCategoria() == categoria)
                .toList();
    }

    public void exibirResumo() {
        System.out.println("=".repeat(45));
        System.out.println("         RESUMO DA CARTEIRA");
        System.out.println("=".repeat(45));
        System.out.printf("  Receitas:  R$ %10.2f%n", totalReceitas());
        System.out.printf("  Despesas:  R$ %10.2f%n", totalDespesas());
        System.out.println("-".repeat(45));
        System.out.printf("  Saldo:     R$ %10.2f%n", calcularSaldo());
        System.out.println("=".repeat(45));
    }
}