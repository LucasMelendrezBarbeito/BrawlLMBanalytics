package com.brawllmbanalytics.dto;


public record VincularCuentaRequest(
    Integer usuarioId,
    String tag,
    String nombre,
    Integer trofeos,
    Integer nivel
) {
}
  
