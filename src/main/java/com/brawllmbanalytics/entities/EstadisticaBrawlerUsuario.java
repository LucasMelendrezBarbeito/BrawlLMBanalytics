package com.brawllmbanalytics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estadisticas_brawler_usuario")

public class EstadisticaBrawlerUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer trofeos;
    private Integer maxTrofeos;
    private Integer poder;
    private Integer rango;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrofeos() {
        return trofeos;
    }

    public void setTrofeos(Integer trofeos) {
        this.trofeos = trofeos;
    }

    public Integer getMaxTrofeos() {
        return maxTrofeos;
    }

    public void setMaxTrofeos(Integer maxTrofeos) {
        this.maxTrofeos = maxTrofeos;
    }

    public Integer getPoder() {
        return poder;
    }

    public void setPoder(Integer poder) {
        this.poder = poder;
    }

    public Integer getRango() {
        return rango;
    }

    public void setRango(Integer rango) {
        this.rango = rango;
    }

    public CuentaBrawl getCuentaBrawl() {
        return cuentaBrawl;
    }

    public void setCuentaBrawl(CuentaBrawl cuentaBrawl) {
        this.cuentaBrawl = cuentaBrawl;
    }

    public Brawler getBrawler() {
        return brawler;
    }

    public void setBrawler(Brawler brawler) {
        this.brawler = brawler;
    }

    public EstadisticaBrawlerUsuario() {
        // Constructor vacío requerido por JPA / Hibernate
    }

    public EstadisticaBrawlerUsuario(Integer trofeos, Integer maxTrofeos, Integer poder, Integer rango,
            CuentaBrawl cuentaBrawl, Brawler brawler) {
        this.trofeos = trofeos;
        this.maxTrofeos = maxTrofeos;
        this.poder = poder;
        this.rango = rango;
        this.cuentaBrawl = cuentaBrawl;
        this.brawler = brawler;
    }

    @ManyToOne
    @JoinColumn(name = "cuenta_brawl_id")
    private CuentaBrawl cuentaBrawl;

    @ManyToOne
    @JoinColumn(name = "brawler_id")
    private Brawler brawler;
}