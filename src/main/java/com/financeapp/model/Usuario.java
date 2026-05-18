package com.financeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.financeapp.exception.SaldoInsuficienteException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transacao> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Meta> metas = new ArrayList<>();

    public Usuario(String nome, String email) {
        validarNome(nome);
        validarEmail(email);
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.email = email;
        this.senha = "";
    }

    public Usuario(String nome, String email, String senha) {
        validarNome(nome);
        validarEmail(email);
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public static Usuario reconstituir(String id, String nome, String email) {
        Usuario u = new Usuario();
        u.id = id;
        u.nome = nome;
        u.email = email;
        u.senha = "";
        return u;
    }

    protected Usuario() {}

    public void adicionarReceita(BigDecimal valor, String descricao, Categoria categoria) {
        Receita receita = new Receita(valor, descricao, categoria);
        receita.setUsuario(this);
        transacoes.add(receita);
    }

    public void adicionarDespesa(BigDecimal valor, String descricao, Categoria categoria) {
        BigDecimal saldoAtual = calcularSaldo();
        if (valor.compareTo(saldoAtual) > 0) {
            throw new SaldoInsuficienteException(saldoAtual, valor);
        }
        Despesa despesa = new Despesa(valor, descricao, categoria);
        despesa.setUsuario(this);
        transacoes.add(despesa);
    }

    public void adicionarMeta(Meta meta) {
        if (meta == null) throw new IllegalArgumentException("Meta não pode ser nula.");
        boolean jaExiste = metas.stream()
                .anyMatch(m -> m.getCategoria() == meta.getCategoria());
        if (jaExiste) throw new IllegalArgumentException(
                "Já existe uma meta para " + meta.getCategoria().exibir());
        meta.setUsuario(this);
        metas.add(meta);
    }

    public BigDecimal calcularSaldo() {
        BigDecimal receitas = transacoes.stream()
                .filter(t -> t instanceof Receita)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal despesas = transacoes.stream()
                .filter(t -> t instanceof Despesa)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return receitas.subtract(despesas);
    }

    public void exibirResumo() {
        System.out.println("\nUsuário: " + nome + " | " + email);
    }

    @JsonIgnore
    public Carteira getCarteira() {
        Carteira carteira = new Carteira();
        transacoes.forEach(carteira::adicionarTransacao);
        return carteira;
    }

    public List<Transacao> getTransacoes()  { return Collections.unmodifiableList(transacoes); }
    public List<Meta> getMetas()            { return Collections.unmodifiableList(metas); }
    public String getId()                   { return id; }
    public String getNome()                 { return nome; }
    public String getEmail()                { return email; }

    public void setNome(String nome)   { validarNome(nome); this.nome = nome; }
    public void setEmail(String email) { validarEmail(email); this.email = email; }

    @JsonIgnore
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio.");
    }

    private void validarEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Email inválido.");
    }
}