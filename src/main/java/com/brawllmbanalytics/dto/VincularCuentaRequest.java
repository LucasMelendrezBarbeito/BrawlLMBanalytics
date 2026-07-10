package com.brawllmbanalytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record VincularCuentaRequest(
    @NotBlank String tag,
    @NotBlank String nombre,
    @NotNull @PositiveOrZero Integer trofeos,
    @NotNull @PositiveOrZero Integer nivel
) {
}
