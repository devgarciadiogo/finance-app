package com.financeapp;

import com.financeapp.model.Categoria;
import com.financeapp.model.Meta;
import com.financeapp.model.Usuario;
import com.financeapp.repository.UsuarioRepository;
import com.financeapp.repository.UsuarioRepositoryJson;
import com.financeapp.service.RelatorioService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        UsuarioRepository repository = new UsuarioRepositoryJson();

        // Cria e salva um usuário
        Usuario usuario = new Usuario("Diogo", "diogo@email.com");
        usuario.adicionarReceita(new BigDecimal("5000.00"), "Salário abril", Categoria.SALARIO);
        usuario.adicionarReceita(new BigDecimal("800.00"),  "Freela site",   Categoria.FREELANCE);
        usuario.adicionarDespesa(new BigDecimal("1200.00"), "Aluguel",       Categoria.MORADIA);
        usuario.adicionarDespesa(new BigDecimal("450.00"),  "Mercado",       Categoria.ALIMENTACAO);
        usuario.adicionarMeta(new Meta(Categoria.ALIMENTACAO, new BigDecimal("400.00")));

        repository.salvar(usuario);
        System.out.println("Usuário salvo com ID: " + usuario.getId());

        // Busca pelo ID — simulando o programa reiniciando
        System.out.println("\nBuscando usuário salvo...");
        repository.buscarPorId(usuario.getId()).ifPresentOrElse(
                u -> {
                    u.exibirResumo();
                    new RelatorioService(u.getCarteira()).exibirRelatorio();
                },
                () -> System.out.println("Usuário não encontrado.")
        );
    }
}