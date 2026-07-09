package com.brawllmbanalytics.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brawllmbanalytics.dto.AgregarItemTierlistRequest;
import com.brawllmbanalytics.entities.ResenaTierlist;
import com.brawllmbanalytics.entities.Tierlist;
import com.brawllmbanalytics.entities.TierlistItem;
import com.brawllmbanalytics.entities.Usuario;
import com.brawllmbanalytics.repositories.UsuarioRepository;
import com.brawllmbanalytics.security.JwtUtil;
import com.brawllmbanalytics.services.TierlistService;

@RestController
@RequestMapping("/tierlists")
public class TierlistController {

    @Autowired
    private TierlistService tierlistService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/crear")
    public Tierlist crearTierlist(
            @RequestParam Integer usuarioId,
            @RequestParam String nombre) {

        return tierlistService.crearTierlist(usuarioId, nombre);
    }

    @PostMapping("/{id}/agregar-item")
    public TierlistItem agregarItem(
            @PathVariable Integer id,
            @RequestBody AgregarItemTierlistRequest req) {

        return tierlistService.agregarItem(id, req.getBrawlerId(), req.getTier());
    }

    // ⭐ AGREGAR RESEÑA CON PUNTUACIÓN
    @PostMapping("/{id}/review")
    public ResenaTierlist agregarReview(
            @PathVariable Integer id,
            @RequestParam Integer usuarioId,
            @RequestParam String comentario,
            @RequestParam Integer puntuacion) {

        return tierlistService.agregarReview(id, usuarioId, comentario, puntuacion);
    }

    @GetMapping("")
    public List<Tierlist> listar() {
        return tierlistService.getTodas();
    }

    @GetMapping("/{id}")
    public Tierlist obtener(@PathVariable Integer id) {
        return tierlistService.getTierlist(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarTierlist(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String tokenHeader) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        Usuario user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        tierlistService.borrarTierlist(id, user);
        return ResponseEntity.ok("Tierlist eliminada");
    }
}
