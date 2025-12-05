package com.brawllmbanalytics.services;

import com.brawllmbanalytics.dto.MapaDto;
import com.brawllmbanalytics.dto.TopBrawlerMapaDto;
import com.brawllmbanalytics.entities.Mapa;
import com.brawllmbanalytics.repositories.MapaBrawlerStatsRepository;
import com.brawllmbanalytics.repositories.MapaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapasService {

    private final MapaRepository mapaRepo;
    private final MapaBrawlerStatsRepository statsRepo;
    private final BrawlStarsService brawlStarsService;

    public MapasService(MapaRepository mapaRepo,
                        MapaBrawlerStatsRepository statsRepo,
                        BrawlStarsService brawlStarsService) {
        this.mapaRepo = mapaRepo;
        this.statsRepo = statsRepo;
        this.brawlStarsService = brawlStarsService;
    }

    public List<MapaDto> mapasEnRotacion() {

    List<Map<String, Object>> eventos = brawlStarsService.getEventsRotation();

    if (eventos == null || eventos.isEmpty()) {
        return List.of();
    }

    return eventos.stream().map(wrapper -> {

        Map<String, Object> event;

        if (wrapper.get("event") instanceof Map<?, ?> map) {
            event = (Map<String, Object>) map;
        } else if (wrapper.get("battleEvent") instanceof Map<?, ?> map) {
            event = (Map<String, Object>) map;
        } else {
            event = wrapper;
        }

        Long supercellId = event.get("id") instanceof Number n ? n.longValue() : null;

        String modo = (String) event.getOrDefault("mode", "");
        String nombre = (String) event.getOrDefault("map", "");

        return new MapaDto(
                null,            
                supercellId,     
                nombre,
                modo
        );
    }).toList();
}


    public List<MapaDto> todosLosMapas() {
        return mapaRepo.findAllByOrderByModoAscNombreAsc()
                .stream()
                .map(m -> new MapaDto(
                        m.getId(),
                        m.getSupercellId(),
                        m.getNombre(),
                        m.getModo()
                ))
                .toList();
    }

   
    public List<TopBrawlerMapaDto> topBrawlersMapa(Long mapaId) {

        Mapa mapa = mapaRepo.findById(mapaId)
                .orElseThrow(() -> new RuntimeException("Mapa no encontrado"));

        return statsRepo.findTop10ByMapaOrderByWinRateDesc(mapa)
                .stream()
                .map(s -> new TopBrawlerMapaDto(
                        s.getBrawler().getId().longValue(),
                        s.getBrawler().getNombre(),
                        s.getWinRate(),
                        s.getPickRate(),
                        ""
                ))
                .toList();
    }


  
    public void importarMapasDesdeBrawlAPI() {
        try {
            String url = "https://api.brawlapi.com/v1/maps";

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error llamando a BrawlAPI: " + conn.getResponseCode());
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String json = in.lines().collect(Collectors.joining());
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> root = mapper.readValue(json, Map.class);
            List<Map<String, Object>> items = (List<Map<String, Object>>) root.get("list");

            for (Map<String, Object> item : items) {

                Long scId = ((Number) item.get("id")).longValue();
                String nombre = (String) item.get("name");

                Map<String, Object> gm = (Map<String, Object>) item.get("gameMode");
                String modo = gm != null ? (String) gm.get("name") : null;

                String imagen = "https://cdn.brawlify.com/maps/regular/" + scId + ".png";

                Mapa existente = mapaRepo.findBySupercellId(scId);

                if (existente == null) {
                    existente = new Mapa();
                    existente.setSupercellId(scId);
                }

                existente.setNombre(nombre);
                existente.setModo(modo);
                existente.setImagenUrl(imagen);

                mapaRepo.save(existente);
            }

            System.out.println("IMPORTACIÓN COMPLETADA: " + items.size() + " mapas actualizados.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error importando mapas de BrawlAPI");
        }
    }
}
