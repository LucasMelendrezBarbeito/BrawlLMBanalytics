package com.brawllmbanalytics.services;

import com.brawllmbanalytics.dto.RegisterRequest;
import com.brawllmbanalytics.entities.Usuario;
import com.brawllmbanalytics.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrar(RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario u = new Usuario();
        u.setUsername(request.username());
        u.setEmail(request.email());
        u.setPassword(passwordEncoder.encode(request.password()));
        u.setRol("USER");

        return usuarioRepository.save(u);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}