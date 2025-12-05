package com.brawllmbanalytics.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "star_powers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StarPower {

    @Id
    private Integer id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "brawler_id")
    private Brawler brawler;
}