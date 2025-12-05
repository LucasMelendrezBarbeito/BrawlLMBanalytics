package com.brawllmbanalytics.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "eventos_rotacion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EventoRotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String inicio;
    private String fin;

    @ManyToOne
    @JoinColumn(name = "mapa_id")
    private Mapa mapa;
}
