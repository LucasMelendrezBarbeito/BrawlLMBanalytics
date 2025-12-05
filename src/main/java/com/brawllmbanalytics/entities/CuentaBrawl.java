package com.brawllmbanalytics.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cuentas_brawl")
public class CuentaBrawl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;
    private String nombre;
    private Integer trofeos;
    private Integer nivel;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
