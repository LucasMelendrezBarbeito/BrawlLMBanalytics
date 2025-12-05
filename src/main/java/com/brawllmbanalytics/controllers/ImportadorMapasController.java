package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.services.MapasService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/mapas")
@CrossOrigin
public class ImportadorMapasController {

    private final MapasService mapasService;

    public ImportadorMapasController(MapasService mapasService) {
        this.mapasService = mapasService;
    }

   
    @GetMapping("/importar")
    public String importar() {
        mapasService.importarMapasDesdeBrawlAPI();
        return "IMPORTACIÓN COMPLETADA";
    }
}
