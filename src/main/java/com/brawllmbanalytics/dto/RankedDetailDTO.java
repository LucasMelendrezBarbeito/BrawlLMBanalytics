package com.brawllmbanalytics.dto;

import java.util.List;

public record RankedDetailDTO(
        Long cuentaId,
        String tag,
        String nombre,
        Ranked currentRank,
        List<RankedSeasonDTO> temporadas,
        List<RankedMatchDTO> ultimasPartidas
) {
    public record Ranked(
            String rank,
            Integer trophies
    ) {}

    public record RankedSeasonDTO(
            Integer seasonNumber,
            String rank,
            Integer trophies
    ) {}
}
