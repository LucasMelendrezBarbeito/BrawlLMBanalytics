package com.brawllmbanalytics.services;

import com.brawllmbanalytics.entities.*;
import com.brawllmbanalytics.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {

    @Autowired
    private EstadisticaBrawlerUsuarioRepository estadisticaRepo;

    @Autowired
    private CuentaBrawlRepository cuentaRepo;

    @Autowired
    private BrawlerRepository brawlerRepo;

    public EstadisticaBrawlerUsuario guardarStats(Integer usuarioId, Integer cuentaBrawlId, Integer brawlerId,
                                                  Integer trofeos, Integer max, Integer poder, Integer rango) {

        CuentaBrawl cuenta = cuentaRepo.findById(cuentaBrawlId)
                .orElseThrow(() -> new RuntimeException("Cuenta Brawl no encontrada"));

        // La cuenta debe pertenecer al usuario autenticado (Principio I: evita IDOR)
        if (cuenta.getUsuario() == null || !cuenta.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("La cuenta no pertenece al usuario autenticado");
        }

        Brawler brawler = brawlerRepo.findById(brawlerId)
                .orElseThrow(() -> new RuntimeException("Brawler no encontrado"));

        EstadisticaBrawlerUsuario e = new EstadisticaBrawlerUsuario();
        e.setCuentaBrawl(cuenta);
        e.setBrawler(brawler);
        e.setTrofeos(trofeos);
        e.setMaxTrofeos(max);
        e.setPoder(poder);
        e.setRango(rango);

        return estadisticaRepo.save(e);
    }
}
