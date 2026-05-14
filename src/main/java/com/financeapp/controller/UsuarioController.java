package com.financeapp.controller;

import com.financeapp.dto.AdicionarMetaRequest;
import com.financeapp.dto.AdicionarTransacaoRequest;
import com.financeapp.dto.CriarUsuarioRequest;
import com.financeapp.model.Usuario;
import com.financeapp.service.RelatorioService;
import com.financeapp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // POST /usuarios — cria um novo usuário
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody @Valid CriarUsuarioRequest request) {
        Usuario usuario = service.criarUsuario(request.nome(), request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    // GET /usuarios — lista todos os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /usuarios/{id} — busca um usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // POST /usuarios/{id}/receitas — adiciona uma receita
    @PostMapping("/{id}/receitas")
    public ResponseEntity<Usuario> adicionarReceita(
            @PathVariable String id,
            @RequestBody @Valid AdicionarTransacaoRequest request) {
        Usuario usuario = service.adicionarReceita(
                id, request.valor(), request.descricao(), request.categoria());
        return ResponseEntity.ok(usuario);
    }

    // POST /usuarios/{id}/despesas — adiciona uma despesa
    @PostMapping("/{id}/despesas")
    public ResponseEntity<Usuario> adicionarDespesa(
            @PathVariable String id,
            @RequestBody @Valid AdicionarTransacaoRequest request) {
        Usuario usuario = service.adicionarDespesa(
                id, request.valor(), request.descricao(), request.categoria());
        return ResponseEntity.ok(usuario);
    }

    // POST /usuarios/{id}/metas — adiciona uma meta
    @PostMapping("/{id}/metas")
    public ResponseEntity<Usuario> adicionarMeta(
            @PathVariable String id,
            @RequestBody @Valid AdicionarMetaRequest request) {
        Usuario usuario = service.adicionarMeta(
                id, request.categoria(), request.valorLimite());
        return ResponseEntity.ok(usuario);
    }

    // GET /usuarios/{id}/relatorio — gera relatório do usuário
    @GetMapping("/{id}/relatorio")
    public ResponseEntity<String> relatorio(@PathVariable String id) {
        Usuario usuario = service.buscarPorId(id);
        RelatorioService relatorio = new RelatorioService(usuario.getCarteira());
        StringBuilder sb = new StringBuilder();
        sb.append("Saldo: R$ ").append(usuario.calcularSaldo()).append("\n");
        relatorio.totalPorCategoria().forEach((cat, total) ->
                sb.append(cat.exibir()).append(": R$ ").append(total).append("\n"));
        return ResponseEntity.ok(sb.toString());
    }

    // DELETE /usuarios/{id} — deleta um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}