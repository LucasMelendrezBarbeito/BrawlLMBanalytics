package com.brawllmbanalytics.dto;


public class AgregarItemTierlistRequest {
    private Integer brawlerId;
    private String tier;
    public Integer getBrawlerId() {
        return brawlerId;
    }
    public void setBrawlerId(Integer brawlerId) {
        this.brawlerId = brawlerId;
    }
    public String getTier() {
        return tier;
    }
    public void setTier(String tier) {
        this.tier = tier;
    }
}