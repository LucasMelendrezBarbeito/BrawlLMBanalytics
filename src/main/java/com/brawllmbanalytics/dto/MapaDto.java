package com.brawllmbanalytics.dto;

public class MapaDto {

    private Long id;            
    private Long supercellId;   
    private String nombre;
    private String modo;
    private String iconUrl;     

    public MapaDto(Long id, Long supercellId, String nombre, String modo) {
        this.id = id;
        this.supercellId = supercellId;
        this.nombre = nombre;
        this.modo = modo;

       
        if (supercellId != null) {
            this.iconUrl = "https://cdn.brawlify.com/maps/regular/" + supercellId + ".png";
        } else {
            
            this.iconUrl = "images/map_default.png";
        }
    }

    

    public Long getId() {
        return id;
    }

    public Long getSupercellId() {
        return supercellId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getModo() {
        return modo;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
