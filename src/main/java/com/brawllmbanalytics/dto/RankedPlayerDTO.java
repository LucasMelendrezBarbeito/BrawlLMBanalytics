package com.brawllmbanalytics.dto;

public record RankedPlayerDTO(
        String tag,
        String name,
        Integer iconId,
        Integer trophies,
        Integer rank,
        String clubName
) {}
