package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.dto.AuthResponse;
import com.brawllmbanalytics.dto.LoginRequest;
import com.brawllmbanalytics.dto.RegisterRequest;
import com.brawllmbanalytics.entities.Usuario;
import com.brawllmbanalytics.security.JwtUtil;
import com.brawllmbanalytics.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Usuario register(@RequestBody RegisterRequest request) {
        return usuarioService.registrar(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();
        String username = user.getUsername();
        String token = jwtUtil.generateToken(username);

        
        Usuario u = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new AuthResponse(token, u.getId(), u.getUsername());
    }
}
