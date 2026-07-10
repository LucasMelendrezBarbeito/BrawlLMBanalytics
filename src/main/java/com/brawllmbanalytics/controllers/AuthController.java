package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.dto.AuthResponse;
import com.brawllmbanalytics.dto.LoginRequest;
import com.brawllmbanalytics.dto.RegisterRequest;
import com.brawllmbanalytics.entities.Usuario;
import com.brawllmbanalytics.security.JwtUtil;
import com.brawllmbanalytics.services.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @PostMapping("/register")
    public Usuario register(@Valid @RequestBody RegisterRequest request) {
        return usuarioService.registrar(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request,
                              HttpServletResponse response) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();
        String username = user.getUsername();
        String token = jwtUtil.generateToken(username);

        
        Usuario u = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (jwtExpirationMs / 1000));
        jwtCookie.setAttribute("SameSite", "Lax");
        response.addCookie(jwtCookie);

        return new AuthResponse(token, u.getId(), u.getUsername());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        jwtCookie.setAttribute("SameSite", "Lax");
        response.addCookie(jwtCookie);

        return ResponseEntity.noContent().build();
    }
}
