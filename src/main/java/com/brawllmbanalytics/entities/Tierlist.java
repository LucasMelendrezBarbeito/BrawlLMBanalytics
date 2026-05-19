package com.brawllmbanalytics.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tierlists")

public class Tierlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({ "tierlists", "resenas" })
    private Usuario usuario;

    @OneToMany(mappedBy = "tierlist", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "tierlist" }) // NO ignorar usuario
    private List<TierlistItem> items;

    @OneToMany(mappedBy = "tierlist", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "tierlist" }) // IMPORTANTE: NO ignorar usuario aquí
    private List<ResenaTierlist> reviews;

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<TierlistItem> getItems() {
        return items;
    }

    public void setItems(List<TierlistItem> items) {
        this.items = items;
    }

    public List<ResenaTierlist> getReviews() {
        return reviews;
    }

    public void setReviews(List<ResenaTierlist> reviews) {
        this.reviews = reviews;
    }

    public Tierlist() {
        // necesario para JPA
    }

    public Tierlist(String nombre, Usuario usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
    }

}
