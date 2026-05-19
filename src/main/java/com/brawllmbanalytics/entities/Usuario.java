package com.brawllmbanalytics.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;

    @Column(nullable = false)
    private String rol = "USER";

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CuentaBrawl> cuentasBrawl;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "usuario", "reviews", "items" })
    private List<Tierlist> tierlists;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "usuario", "tierlist" })
    private List<ResenaTierlist> resenas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<CuentaBrawl> getCuentasBrawl() {
        return cuentasBrawl;
    }

    public void setCuentasBrawl(List<CuentaBrawl> cuentasBrawl) {
        this.cuentasBrawl = cuentasBrawl;
    }

    public List<Tierlist> getTierlists() {
        return tierlists;
    }

    public void setTierlists(List<Tierlist> tierlists) {
        this.tierlists = tierlists;
    }

    public List<ResenaTierlist> getResenas() {
        return resenas;
    }

    public void setResenas(List<ResenaTierlist> resenas) {
        this.resenas = resenas;
    }

    public Usuario() {
        // necesario para JPA
    }

    public Usuario(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
