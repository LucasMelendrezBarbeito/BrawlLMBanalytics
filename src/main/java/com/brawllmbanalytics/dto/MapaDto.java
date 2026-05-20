package com.brawllmbanalytics.dto;

public record MapaDto(
        Long id,
        Long supercellId,
        String nombre,
        String modo,
        String iconUrl) {
    public MapaDto(Long id, Long supercellId, String nombre, String modo) {
        this(
                id,
                supercellId,
                nombre,
                modo,
                (supercellId != null)
                        ? "https://cdn.brawlify.com/maps/regular/" + supercellId + ".png"
                        : "images/map_default.png");
    }
}