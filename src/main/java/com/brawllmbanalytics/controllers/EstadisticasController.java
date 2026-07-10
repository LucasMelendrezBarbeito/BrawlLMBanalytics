package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.dto.GuardarEstadisticaRequest;
import com.brawllmbanalytics.entities.EstadisticaBrawlerUsuario;
import com.brawllmbanalytics.services.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadisticas")
@CrossOrigin
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    @PostMapping
    public EstadisticaBrawlerUsuario guardar(@RequestBody GuardarEstadisticaRequest req) {
        return estadisticasService.guardarStats(
                req.cuentaBrawlId(),
                req.brawlerId(),
                req.trofeos(),
                req.maxTrofeos(),
                req.poder(),
                req.rango()
        );
    }
}

