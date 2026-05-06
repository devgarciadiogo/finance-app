package com.financeapp.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financeapp.model.Usuario;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryJson implements UsuarioRepository {

    private static final String ARQUIVO = "dados/usuarios.json";
    private final ObjectMapper mapper;

    public UsuarioRepositoryJson() {
        this.mapper = new ObjectMapper();
        // Ensina o Jackson a lidar com LocalDate
        this.mapper.registerModule(new JavaTimeModule());
        // Salva datas como texto legível, não como número
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Formata o JSON com indentação para ficar legível
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Cria a pasta "dados" se não existir
        new File("dados").mkdirs();
    }

    @Override
    public void salvar(Usuario usuario) {
        List<Usuario> usuarios = listarTodos();

        // Se já existe, atualiza. Se não existe, adiciona.
        boolean jaExiste = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(usuario.getId())) {
                usuarios.set(i, usuario);
                jaExiste = true;
                break;
            }
        }
        if (!jaExiste) {
            usuarios.add(usuario);
        }

        try {
            mapper.writeValue(new File(ARQUIVO), usuarios);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar usuário no arquivo.", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return listarTodos().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return listarTodos().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Usuario> listarTodos() {
        File arquivo = new File(ARQUIVO);

        // Se o arquivo não existe ainda, retorna lista vazia
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(arquivo,
                    mapper.getTypeFactory().constructCollectionType(List.class, Usuario.class));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo de usuários.", e);
        }
    }

    @Override
    public void deletar(String id) {
        List<Usuario> usuarios = listarTodos();
        usuarios.removeIf(u -> u.getId().equals(id));

        try {
            mapper.writeValue(new File(ARQUIVO), usuarios);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar usuário.", e);
        }
    }
}