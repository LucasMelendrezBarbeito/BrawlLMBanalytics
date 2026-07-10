package com.brawllmbanalytics.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record GuardarEstadisticaRequest (
    @NotNull Integer cuentaBrawlId,
    @NotNull Integer brawlerId,
    @NotNull @PositiveOrZero Integer trofeos,
    @NotNull @PositiveOrZero Integer maxTrofeos,
    @NotNull @PositiveOrZero Integer poder,
    @NotNull @PositiveOrZero Integer rango
){}
