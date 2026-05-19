package com.brawllmbanalytics.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.brawllmbanalytics.services.BrawlStarsService;

@RestController
@RequestMapping("/brawl")
@CrossOrigin
public class BrawlController {

    private final BrawlStarsService brawlService;

    public BrawlController(BrawlStarsService brawlService) {
        this.brawlService = brawlService;
    }

 
    @GetMapping("/ip-publica")
    public String ipPublica() {
        try {
            RestTemplate rest = new RestTemplate();
            return "IP pública detectada por Spring Boot: " +
                    rest.getForObject("https://checkip.amazonaws.com/", String.class).trim();
        } catch (Exception ex) {
            return "Error obteniendo IP: " + ex.getMessage();
        }
    }

    
    @GetMapping("/player")
    public Map<String, Object> getPlayer(@RequestParam String tag) {
        return brawlService.getPlayerData(tag);
    }

 
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {

        String message = ex.getMessage() != null ? ex.getMessage() : "";

        if (message.contains("404") || message.contains("NOT_FOUND")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Jugador de Brawl Stars no encontrado con ese tag."));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error interno al contactar la API: " + message));
    }
}
