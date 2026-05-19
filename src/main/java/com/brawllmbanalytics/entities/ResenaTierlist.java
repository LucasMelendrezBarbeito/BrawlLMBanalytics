package com.brawllmbanalytics.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resenas_tierlist")

public class ResenaTierlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer puntuacion;

    private String comentario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Tierlist getTierlist() {
        return tierlist;
    }

    public void setTierlist(Tierlist tierlist) {
        this.tierlist = tierlist;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ResenaTierlist() {
        // necesario para JPA
    }

    public ResenaTierlist(Integer puntuacion, String comentario, Tierlist tierlist, Usuario usuario) {
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.tierlist = tierlist;
        this.usuario = usuario;
    }

    @ManyToOne
    @JoinColumn(name = "tierlist_id")
    @JsonIgnoreProperties({ "reviews", "items" })
    private Tierlist tierlist;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({ "tierlists", "resenas" })
    private Usuario usuario;
}
