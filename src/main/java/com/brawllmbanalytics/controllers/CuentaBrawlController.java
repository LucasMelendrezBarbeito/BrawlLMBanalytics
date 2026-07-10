package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.dto.VincularCuentaRequest;
import com.brawllmbanalytics.entities.CuentaBrawl;
import com.brawllmbanalytics.entities.Usuario;
import com.brawllmbanalytics.repositories.CuentaBrawlRepository;
import com.brawllmbanalytics.repositories.UsuarioRepository;
import com.brawllmbanalytics.security.JwtUtil;
import com.brawllmbanalytics.services.CuentaBrawlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@CrossOrigin
public class CuentaBrawlController {

    @Autowired
    private CuentaBrawlService cuentaBrawlService;

    @Autowired
    private CuentaBrawlRepository cuentaBrawlRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/vincular")
    public CuentaBrawl vincular(@Valid @RequestBody VincularCuentaRequest req,
                                @RequestHeader("Authorization") String tokenHeader) {
        Usuario user = usuarioDesdeToken(tokenHeader);
        return cuentaBrawlService.vincularCuenta(
                user.getId(),
                req.tag(),
                req.nombre(),
                req.trofeos(),
                req.nivel()
        );
    }

    // Devuelve solo las cuentas del usuario autenticado (el id sale del token, no de la URL)
    @GetMapping("/mias")
    public List<CuentaBrawl> misCuentas(@RequestHeader("Authorization") String tokenHeader) {
        Usuario user = usuarioDesdeToken(tokenHeader);
        return cuentaBrawlRepository.findByUsuarioId(user.getId());
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id,
                         @RequestHeader("Authorization") String tokenHeader) {
        Usuario user = usuarioDesdeToken(tokenHeader);
        cuentaBrawlService.eliminarCuenta(id, user.getId());
    }

    private Usuario usuarioDesdeToken(String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
