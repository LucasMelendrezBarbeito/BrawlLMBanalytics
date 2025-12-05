package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.dto.MapaDto;
import com.brawllmbanalytics.dto.TopBrawlerMapaDto;
import com.brawllmbanalytics.services.MapasService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mapas")
@CrossOrigin
public class MapasController {

    private final MapasService mapasService;

    public MapasController(MapasService mapasService) {
        this.mapasService = mapasService;
    }

    @GetMapping("/rotacion")
    public List<MapaDto> mapasEnRotacion() {
        return mapasService.mapasEnRotacion();
    }

    @GetMapping
    public List<MapaDto> todosLosMapas() {
        return mapasService.todosLosMapas();
    }

    @GetMapping("/{id}/top-brawlers")
    public List<TopBrawlerMapaDto> topBrawlers(@PathVariable Long id) {
        return mapasService.topBrawlersMapa(id);
    }
}