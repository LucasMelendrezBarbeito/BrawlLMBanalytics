package com.brawllmbanalytics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GuardarEstadisticaRequest {
    private Integer cuentaBrawlId;
    private Integer brawlerId;
    private Integer trofeos;
    private Integer maxTrofeos;
    private Integer poder;
    private Integer rango;
}
