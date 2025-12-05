package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.entities.Brawler;
import com.brawllmbanalytics.repositories.BrawlerRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/brawl")
@CrossOrigin
public class BrawlerController {

    private final BrawlerRepository brawlerRepository;

    public BrawlerController(BrawlerRepository brawlerRepository) {
        this.brawlerRepository = brawlerRepository;
    }

    @GetMapping("/brawlers")
public List<Brawler> getBrawlers() {
    List<Brawler> lista = brawlerRepository.findAll();
    lista.forEach(b -> System.out.println("Brawler " + b.getNombre() + " → " + b.getIconUrl()));
    return lista;
}
    @GetMapping("/brawlers/{id}")
    public Brawler getBrawler(@PathVariable Integer id) {
        return brawlerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brawler no encontrado"));
    }
}
