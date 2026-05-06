package com.financeapp.repository;

import com.financeapp.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    //Salva ou atualiza um usuário
    void salvar(Usuario usuario);

    //Busca por ID
    Optional<Usuario> buscarPorId(String id);

    //Busca por email
    Optional<Usuario> buscarPorEmail(String email);

    //Retorna todos os usuários
    List<Usuario> listarTodos();

    //Remove por ID
    void deletar(String id);

}


