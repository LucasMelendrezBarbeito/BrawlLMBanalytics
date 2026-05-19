package com.brawllmbanalytics.dto;


public class GuardarEstadisticaRequest {
    private Integer cuentaBrawlId;
    private Integer brawlerId;
    private Integer trofeos;
    private Integer maxTrofeos;
    private Integer poder;
    private Integer rango;
    public Integer getCuentaBrawlId() {
        return cuentaBrawlId;
    }
    public void setCuentaBrawlId(Integer cuentaBrawlId) {
        this.cuentaBrawlId = cuentaBrawlId;
    }
    public Integer getBrawlerId() {
        return brawlerId;
    }
    public void setBrawlerId(Integer brawlerId) {
        this.brawlerId = brawlerId;
    }
    public Integer getTrofeos() {
        return trofeos;
    }
    public void setTrofeos(Integer trofeos) {
        this.trofeos = trofeos;
    }
    public Integer getMaxTrofeos() {
        return maxTrofeos;
    }
    public void setMaxTrofeos(Integer maxTrofeos) {
        this.maxTrofeos = maxTrofeos;
    }
    public Integer getPoder() {
        return poder;
    }
    public void setPoder(Integer poder) {
        this.poder = poder;
    }
    public Integer getRango() {
        return rango;
    }
    public void setRango(Integer rango) {
        this.rango = rango;
    }
}
