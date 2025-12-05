package com.brawllmbanalytics.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estadisticas_brawler_usuario")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EstadisticaBrawlerUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer trofeos;
    private Integer maxTrofeos;
    private Integer poder;
    private Integer rango;

    @ManyToOne
    @JoinColumn(name = "cuenta_brawl_id")
    private CuentaBrawl cuentaBrawl;

    @ManyToOne
    @JoinColumn(name = "brawler_id")
    private Brawler brawler;
}