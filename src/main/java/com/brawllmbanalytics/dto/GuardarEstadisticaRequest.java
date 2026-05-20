package com.brawllmbanalytics.dto;

public record GuardarEstadisticaRequest (
    Integer cuentaBrawlId,
    Integer brawlerId,
    Integer trofeos,
    Integer maxTrofeos,
    Integer poder,
    Integer rango
){}