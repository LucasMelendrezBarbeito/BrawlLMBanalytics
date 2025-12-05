package com.brawllmbanalytics.dto;

public class TopBrawlerMapaDto {
    private Long brawlerId;
    private String brawlerNombre;  
    private double winRate;
    private double pickRate;
    private String iconoUrl;

    public TopBrawlerMapaDto(Long brawlerId,
                             String brawlerNombre,
                             double winRate,
                             double pickRate,
                             String iconoUrl) {
        this.brawlerId = brawlerId;
        this.brawlerNombre = brawlerNombre;
        this.winRate = winRate;
        this.pickRate = pickRate;
        this.iconoUrl = iconoUrl;
    }

    public Long getBrawlerId() {
        return brawlerId;
    }

    public String getBrawlerNombre() {  
        return brawlerNombre;
    }

    public double getWinRate() {
        return winRate;
    }

    public double getPickRate() {
        return pickRate;
    }

    public String getIconoUrl() {
        return iconoUrl;
    }
}
