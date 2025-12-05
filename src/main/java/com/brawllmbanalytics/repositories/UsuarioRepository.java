package com.brawllmbanalytics.repositories;

import com.brawllmbanalytics.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
}