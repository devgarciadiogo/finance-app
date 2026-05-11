package com.financeapp;

import com.financeapp.model.*;
import com.financeapp.repository.UsuarioRepository;
import com.financeapp.repository.UsuarioRepositoryJdbc;
import com.financeapp.service.RelatorioService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        UsuarioRepository repository = new UsuarioRepositoryJdbc();

        Usuario usuario = new Usuario("Diogo", "diogo@email.com");
        usuario.adicionarReceita(new BigDecimal("5000.00"), "Salário abril",  Categoria.SALARIO);
        usuario.adicionarReceita(new BigDecimal("800.00"),  "Freela site",    Categoria.FREELANCE);
        usuario.adicionarDespesa(new BigDecimal("1200.00"), "Aluguel",        Categoria.MORADIA);
        usuario.adicionarDespesa(new BigDecimal("450.00"),  "Mercado",        Categoria.ALIMENTACAO);
        usuario.adicionarMeta(new Meta(Categoria.ALIMENTACAO, new BigDecimal("400.00")));

        repository.salvar(usuario);
        System.out.println("Salvo no banco com ID: " + usuario.getId());

        System.out.println("\nBuscando do banco...");
        repository.buscarPorId(usuario.getId()).ifPresentOrElse(
                u -> {
                    u.exibirResumo();
                    new RelatorioService(u.getCarteira()).exibirRelatorio();
                },
                () -> System.out.println("Não encontrado.")
        );
    }
}