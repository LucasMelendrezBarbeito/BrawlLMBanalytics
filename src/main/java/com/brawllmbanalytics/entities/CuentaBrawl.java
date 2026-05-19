package com.brawllmbanalytics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity

@Table(name = "cuentas_brawl")
public class CuentaBrawl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;
    private String nombre;
    private Integer trofeos;
    private Integer nivel;
    

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getTag() {
        return tag;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Integer getTrofeos() {
        return trofeos;
    }


    public void setTrofeos(Integer trofeos) {
        this.trofeos = trofeos;
    }


    public Integer getNivel() {
        return nivel;
    }


    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }


    public Usuario getUsuario() {
        return usuario;
    }


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
