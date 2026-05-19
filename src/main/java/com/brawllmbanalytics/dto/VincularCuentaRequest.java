package com.brawllmbanalytics.dto;


public class VincularCuentaRequest {
    private Integer usuarioId;
    private String tag;
    private String nombre;
    private Integer trofeos;
    private Integer nivel;
    public Integer getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
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
}