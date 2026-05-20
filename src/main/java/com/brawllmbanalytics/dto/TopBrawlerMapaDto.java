package com.brawllmbanalytics.dto;

public record TopBrawlerMapaDto(
    Long brawlerId,
    String brawlerNombre,
    double winRate,
    double pickRate,
    String iconoUrl
) {}