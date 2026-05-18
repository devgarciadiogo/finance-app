package com.financeapp.service;

import com.financeapp.dto.LoginRequest;
import com.financeapp.dto.RegisterRequest;
import com.financeapp.dto.TokenResponse;
import com.financeapp.infra.JwtService;
import com.financeapp.model.Usuario;
import com.financeapp.repository.UsuarioJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioJpaRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioJpaRepository repository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public TokenResponse registrar(RegisterRequest request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        // Hasheia a senha antes de salvar - nunca salva senha em texto puro
        String senhaHasheada = passwordEncoder.encode(request.senha());

        Usuario usuario = new Usuario(request.nome(), request.email(), senhaHasheada);
        repository.save(usuario);

        String token = jwtService.gerarToken(request.email());
        return new TokenResponse(token, request.email());
    }

    public TokenResponse login(LoginRequest request) {
        Usuario usuario = repository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos."));

        // Compara a senha digitada com o hash salvo no banco
        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Email ou senha inválidos.");
        }

        String token = jwtService.gerarToken(request.email());
        return new TokenResponse(token, request.email());
    }
}