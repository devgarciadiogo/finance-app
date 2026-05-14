package com.financeapp.repository;

import com.financeapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);
}