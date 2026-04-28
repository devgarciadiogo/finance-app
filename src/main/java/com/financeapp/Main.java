package com.financeapp;

import com.financeapp.exception.SaldoInsuficienteException;
import com.financeapp.model.Carteira;
import com.financeapp.model.Categoria;
import com.financeapp.model.Meta;
import com.financeapp.model.Usuario;
import com.financeapp.service.RelatorioService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        Usuario usuario = new Usuario("Carlos", "carlos@email.com");

        // Receitas
        usuario.adicionarReceita(new BigDecimal("5000.00"), "Salário abril", Categoria.SALARIO);
        usuario.adicionarReceita(new BigDecimal("800.00"), "Freela site", Categoria.FREELANCE);

        // Despesas
        usuario.adicionarDespesa(new BigDecimal("1200.00"), "Aluguel", Categoria.MORADIA);
        usuario.adicionarDespesa(new BigDecimal("450.00"), "Mercado", Categoria.ALIMENTACAO);
        usuario.adicionarDespesa(new BigDecimal("120.00"), "Uber do mês", Categoria.TRANSPORTE);
        usuario.adicionarDespesa(new BigDecimal("89.90"), "Farmácia", Categoria.SAUDE);
        usuario.adicionarDespesa(new BigDecimal("200.00"), "Curso Java", Categoria.EDUCACAO);

        // Metas
        usuario.adicionarMeta(new Meta(Categoria.ALIMENTACAO, new BigDecimal("400.00")));
        usuario.adicionarMeta(new Meta(Categoria.MORADIA, new BigDecimal("1500.00")));
        usuario.adicionarMeta(new Meta(Categoria.TRANSPORTE, new BigDecimal("150.00")));
        usuario.adicionarMeta(new Meta(Categoria.SAUDE, new BigDecimal("100.00")));

        // Resumo
        usuario.exibirResumo();

        // Relatório
        RelatorioService relatorio = new RelatorioService(usuario.getCarteira());
        relatorio.exibirRelatorio();

        // Verificação de metas
        relatorio.verificarMetas(usuario.getMetas());
    }
}