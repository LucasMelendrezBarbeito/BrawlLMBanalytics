package com.brawllmbanalytics.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "brawlers")

public class Brawler {

    @Id
    private Integer id;

    private String nombre;
    private String rareza;
    private String descripcion;

    // URL del icono; no se persiste, se calcula en runtime desde el id (ver TierlistService)
    @Transient
    private String iconUrl;

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

    public String getRareza() {
        return rareza;
    }

    public void setRareza(String rareza) {
        this.rareza = rareza;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<Gadget> getGadgets() {
        return gadgets;
    }

    public void setGadgets(List<Gadget> gadgets) {
        this.gadgets = gadgets;
    }

    public List<StarPower> getStarPowers() {
        return starPowers;
    }

    public void setStarPowers(List<StarPower> starPowers) {
        this.starPowers = starPowers;
    }

    public Brawler() {
        // Constructor vacío requerido por JPA / Hibernate
    }

    public Brawler(Integer id, String nombre, String rareza, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.rareza = rareza;
        this.descripcion = descripcion;
    }

    @OneToMany(mappedBy = "brawler", cascade = CascadeType.ALL)
    private List<Gadget> gadgets;

    @OneToMany(mappedBy = "brawler", cascade = CascadeType.ALL)
    private List<StarPower> starPowers;

}