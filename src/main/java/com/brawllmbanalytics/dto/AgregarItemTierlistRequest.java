package com.brawllmbanalytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AgregarItemTierlistRequest (
    @NotNull Integer brawlerId,
    @NotBlank @Size(max = 5) String tier
) {
}
