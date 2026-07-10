package com.brawllmbanalytics.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brawllmbanalytics.entities.Brawler;
import com.brawllmbanalytics.repositories.BrawlerRepository;

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
        lista.forEach(b -> b.setIconUrl("https://cdn.brawlify.com/brawlers/borderless/" + b.getId() + ".png"));
        return lista;
    }
    @GetMapping("/brawlers/{id}")
    public Brawler getBrawler(@PathVariable Integer id) {
        return brawlerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brawler no encontrado"));
    }
}
