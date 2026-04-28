package com.financeapp.service;

import com.financeapp.model.Carteira;
import com.financeapp.model.Categoria;
import com.financeapp.model.Despesa;
import com.financeapp.model.Receita;
import com.financeapp.model.Transacao;
import com.financeapp.model.Meta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

public class RelatorioService {

    private final Carteira carteira;

    public RelatorioService(Carteira carteira) {
        this.carteira = carteira;
    }

    // Maior despesa
    public Optional<Transacao> maiorDespesa() {
        return carteira.getTransacoes().stream()
                .filter(t -> t instanceof Despesa)
                .max(Comparator.comparing(Transacao::getValor));
    }

    // Maior receita
    public Optional<Transacao> maiorReceita() {
        return carteira.getTransacoes().stream()
                .filter(t -> t instanceof Receita)
                .max(Comparator.comparing(Transacao::getValor));
    }

    // Total gasto por categoria
    public Map<Categoria, BigDecimal> totalPorCategoria() {
        return carteira.getTransacoes().stream()
                .filter(t -> t instanceof Despesa)
                .collect(Collectors.groupingBy(
                        Transacao::getCategoria,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transacao::getValor,
                                BigDecimal::add
                        )
                ));
    }

    // Média das despesas
    public BigDecimal mediaDespesas() {
        return carteira.getTransacoes().stream()
                .filter(t -> t instanceof Despesa)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(
                        BigDecimal.valueOf(
                                carteira.getTransacoes().stream()
                                        .filter(t -> t instanceof Despesa)
                                        .count()
                        ),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    // Exibe relatório completo
    public void exibirRelatorio() {
        System.out.println("\n" + "=".repeat(45));
        System.out.println("            RELATÓRIO DETALHADO");
        System.out.println("=".repeat(45));

        // Maior despesa
        maiorDespesa().ifPresentOrElse(
                t -> System.out.printf("  Maior despesa:  %s (R$ %.2f)%n",
                        t.getDescricao(), t.getValor()),
                () -> System.out.println("  Nenhuma despesa registrada.")
        );

        // Maior receita
        maiorReceita().ifPresentOrElse(
                t -> System.out.printf("  Maior receita:  %s (R$ %.2f)%n",
                        t.getDescricao(), t.getValor()),
                () -> System.out.println("  Nenhuma receita registrada.")
        );

        // Média das despesas
        System.out.printf("  Média despesas: R$ %.2f%n", mediaDespesas());

        // Total por categoria
        System.out.println("\n  GASTOS POR CATEGORIA:");
        System.out.println("  " + "-".repeat(35));
        totalPorCategoria().forEach((categoria, total) ->
                System.out.printf("  %-15s R$ %10.2f%n",
                        categoria.exibir(), total)
        );

        System.out.println("=".repeat(45));
    }

    public void verificarMetas(List<Meta> metas) {
        System.out.println("\n" + "=".repeat(45));
        System.out.println("          VERIFICAÇÃO DE METAS");
        System.out.println("=".repeat(45));

        Map<Categoria, BigDecimal> gastosPorCategoria = totalPorCategoria();

        for (Meta meta : metas) {
            BigDecimal gasto = gastosPorCategoria
                    .getOrDefault(meta.getCategoria(), BigDecimal.ZERO);

            BigDecimal percentual = gasto
                    .divide(meta.getValorLimite(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            String status = gasto.compareTo(meta.getValorLimite()) > 0
                    ? "❌ ULTRAPASSOU"
                    : percentual.compareTo(BigDecimal.valueOf(80)) >= 0
                    ? "⚠️  ATENÇÃO"
                    : "✅ OK";

            System.out.printf("  %-15s R$ %8.2f / R$ %8.2f  %s (%.0f%%)%n",
                    meta.getCategoria().exibir(),
                    gasto,
                    meta.getValorLimite(),
                    status,
                    percentual);
        }

        System.out.println("=".repeat(45));
    }
}