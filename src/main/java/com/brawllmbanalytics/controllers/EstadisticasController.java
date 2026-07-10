package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.dto.GuardarEstadisticaRequest;
import com.brawllmbanalytics.entities.EstadisticaBrawlerUsuario;
import com.brawllmbanalytics.entities.Usuario;
import com.brawllmbanalytics.repositories.UsuarioRepository;
import com.brawllmbanalytics.security.JwtUtil;
import com.brawllmbanalytics.services.EstadisticasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadisticas")
@CrossOrigin
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public EstadisticaBrawlerUsuario guardar(@Valid @RequestBody GuardarEstadisticaRequest req,
                                             @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        Usuario user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return estadisticasService.guardarStats(
                user.getId(),
                req.cuentaBrawlId(),
                req.brawlerId(),
                req.trofeos(),
                req.maxTrofeos(),
                req.poder(),
                req.rango()
        );
    }
}

