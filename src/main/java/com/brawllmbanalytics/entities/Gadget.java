package com.brawllmbanalytics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "gadgets")

public class Gadget {

    @Id
    private Integer id;

    private String nombre;

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

    @ManyToOne
    @JoinColumn(name = "brawler_id")
    private Brawler brawler;
}
