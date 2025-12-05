package com.brawllmbanalytics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AgregarItemTierlistRequest {
    private Integer brawlerId;
    private String tier;
}