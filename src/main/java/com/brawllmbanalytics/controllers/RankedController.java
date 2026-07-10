package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.dto.RankedDetailDTO;
import com.brawllmbanalytics.dto.RankedPlayerDTO;
import com.brawllmbanalytics.entities.Usuario;
import com.brawllmbanalytics.repositories.UsuarioRepository;
import com.brawllmbanalytics.security.JwtUtil;
import com.brawllmbanalytics.services.RankedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rankeds")
public class RankedController {

    private final RankedService rankedService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public RankedController(RankedService rankedService, JwtUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.rankedService = rankedService;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/global")
    public List<RankedPlayerDTO> globalRankings() {
        return rankedService.getGlobalRankings();
    }

    @GetMapping("/local/{countryCode}")
    public List<RankedPlayerDTO> localRankings(@PathVariable String countryCode) {
        return rankedService.getLocalRankings(countryCode);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public RankedDetailDTO cuentaRankedDetail(@PathVariable Long cuentaId,
                                              @RequestHeader("Authorization") String tokenHeader) {
        Usuario user = usuarioDesdeToken(tokenHeader);
        return rankedService.getCuentaRankedDetail(cuentaId, user.getId());
    }

    @PostMapping("/cuenta/{cuentaId}/sync")
    public void syncCuentaRanked(@PathVariable Long cuentaId,
                                 @RequestHeader("Authorization") String tokenHeader) {
        Usuario user = usuarioDesdeToken(tokenHeader);
        rankedService.syncCuentaRanked(cuentaId, user.getId());
    }

    private Usuario usuarioDesdeToken(String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
