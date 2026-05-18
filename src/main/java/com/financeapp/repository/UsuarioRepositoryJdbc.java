package com.financeapp.repository;

import com.financeapp.infra.ConexaoBanco;
import com.financeapp.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryJdbc implements UsuarioRepository {

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nome, email) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET nome = ?, email = ?";

        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getNome());
            stmt.setString(5, usuario.getEmail());
            stmt.executeUpdate();

            salvarTransacoes(conn, usuario);
            salvarMetas(conn, usuario);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário.", e);
        }
    }

    private void salvarTransacoes(Connection conn, Usuario usuario) throws SQLException {
        String deleteSql = "DELETE FROM transacoes WHERE usuario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
            stmt.setString(1, usuario.getId());
            stmt.executeUpdate();
        }

        String insertSql = "INSERT INTO transacoes (id, usuario_id, tipo, valor, descricao, categoria, data) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            for (Transacao t : usuario.getCarteira().getTransacoes()) {
                stmt.setString(1, t.getId());
                stmt.setString(2, usuario.getId());
                stmt.setString(3, t.getTipo());
                stmt.setBigDecimal(4, t.getValor());
                stmt.setString(5, t.getDescricao());
                stmt.setString(6, t.getCategoria().name());
                stmt.setDate(7, Date.valueOf(t.getData()));
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void salvarMetas(Connection conn, Usuario usuario) throws SQLException {
        String deleteSql = "DELETE FROM metas WHERE usuario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
            stmt.setString(1, usuario.getId());
            stmt.executeUpdate();
        }

        String insertSql = "INSERT INTO metas (id, usuario_id, categoria, valor_limite) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            for (Meta m : usuario.getMetas()) {
                stmt.setString(1, m.getId());
                stmt.setString(2, usuario.getId());
                stmt.setString(3, m.getCategoria().name());
                stmt.setBigDecimal(4, m.getValorLimite());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = construirUsuario(rs);
                carregarTransacoes(conn, usuario);
                carregarMetas(conn, usuario);
                return Optional.of(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário.", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = construirUsuario(rs);
                carregarTransacoes(conn, usuario);
                carregarMetas(conn, usuario);
                return Optional.of(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = construirUsuario(rs);
                carregarTransacoes(conn, usuario);
                carregarMetas(conn, usuario);
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários.", e);
        }

        return usuarios;
    }

    @Override
    public void deletar(String id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário.", e);
        }
    }

    private Usuario construirUsuario(ResultSet rs) throws SQLException {
        return Usuario.reconstituir(
                rs.getString("id"),
                rs.getString("nome"),
                rs.getString("email")
        );
    }

    private void carregarTransacoes(Connection conn, Usuario usuario) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tipo      = rs.getString("tipo");
                BigDecimal valor = rs.getBigDecimal("valor");
                String descricao = rs.getString("descricao");
                Categoria cat    = Categoria.valueOf(rs.getString("categoria"));

                Transacao t = tipo.equals("RECEITA")
                        ? new Receita(valor, descricao, cat)
                        : new Despesa(valor, descricao, cat);

                usuario.getCarteira().adicionarTransacao(t);
            }
        }
    }

    private void carregarMetas(Connection conn, Usuario usuario) throws SQLException {
        String sql = "SELECT * FROM metas WHERE usuario_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Categoria cat     = Categoria.valueOf(rs.getString("categoria"));
                BigDecimal limite = rs.getBigDecimal("valor_limite");
                usuario.adicionarMeta(new Meta(cat, limite));
            }
        }
    }
}