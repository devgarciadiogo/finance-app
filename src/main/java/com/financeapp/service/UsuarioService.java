package com.financeapp.service;

import com.financeapp.exception.TransacaoNaoEncontradaException;
import com.financeapp.model.*;
import com.financeapp.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioJpaRepository repository;

    // Spring injeta o repository automaticamente — isso é Injeção de Dependência
    public UsuarioService(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Usuario criarUsuario(String nome, String email) {
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado: " + email);
        }
        Usuario usuario = new Usuario(nome, email);
        return repository.save(usuario);
    }

    public Usuario buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(id));
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Usuario adicionarReceita(String usuarioId, BigDecimal valor,
                                    String descricao, Categoria categoria) {
        Usuario usuario = buscarPorId(usuarioId);
        usuario.adicionarReceita(valor, descricao, categoria);
        return repository.save(usuario);
    }

    @Transactional
    public Usuario adicionarDespesa(String usuarioId, BigDecimal valor,
                                    String descricao, Categoria categoria) {
        Usuario usuario = buscarPorId(usuarioId);
        usuario.adicionarDespesa(valor, descricao, categoria);
        return repository.save(usuario);
    }

    @Transactional
    public Usuario adicionarMeta(String usuarioId, Categoria categoria,
                                 BigDecimal valorLimite) {
        Usuario usuario = buscarPorId(usuarioId);
        usuario.adicionarMeta(new Meta(categoria, valorLimite));
        return repository.save(usuario);
    }

    @Transactional
    public void deletarUsuario(String id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}