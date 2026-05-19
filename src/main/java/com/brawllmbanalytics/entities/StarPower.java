package com.brawllmbanalytics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "star_powers")

public class StarPower {

    @Id
    private Integer id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "brawler_id")
    private Brawler brawler;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Brawler getBrawler() {
        return brawler;
    }

    public void setBrawler(Brawler brawler) {
        this.brawler = brawler;
    }

    public StarPower() {
        // Constructor vacío para JPA
    }

    public StarPower(Integer id, String nombre, Brawler brawler) {
        this.id = id;
        this.nombre = nombre;
        this.brawler = brawler;
    }
}