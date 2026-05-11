package com.financeapp.infra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConexaoBanco {

    private static String url;
    private static String user;
    private static String password;

    // Bloco estático — roda uma vez quando a classe é carregada
    static {
        carregarEnv();
    }

    private static void carregarEnv() {
        Map<String, String> env = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Ignora linhas vazias e comentários
                if (linha.isBlank() || linha.startsWith("#")) continue;

                String[] partes = linha.split("=", 2);
                if (partes.length == 2) {
                    env.put(partes[0].trim(), partes[1].trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Arquivo .env não encontrado na raiz do projeto.", e);
        }

        url = env.get("DB_URL");
        user = env.get("DB_USER");
        password = env.get("DB_PASSWORD");
    }

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}