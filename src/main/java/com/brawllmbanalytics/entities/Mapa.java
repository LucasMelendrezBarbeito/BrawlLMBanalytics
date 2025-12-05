package com.brawllmbanalytics.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "mapas")
public class Mapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   
    @Column(name = "supercell_id", unique = true)
    private Long supercellId;  

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = true)
    private String modo;  

    @Column(nullable = true)
    private String imagenUrl;  

    @Column(name = "en_rotacion")
    private boolean enRotacion = false;

    
    public Mapa() {}

    

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupercellId() {
        return supercellId;
    }
    public void setSupercellId(Long supercellId) {
        this.supercellId = supercellId;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModo() {
        return modo;
    }
    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isEnRotacion() {
        return enRotacion;
    }
    public void setEnRotacion(boolean enRotacion) {
        this.enRotacion = enRotacion;
    }
}
