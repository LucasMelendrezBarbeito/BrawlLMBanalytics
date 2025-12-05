package com.brawllmbanalytics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VincularCuentaRequest {
    private Integer usuarioId;
    private String tag;
    private String nombre;
    private Integer trofeos;
    private Integer nivel;
}