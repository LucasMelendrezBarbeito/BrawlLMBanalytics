package com.brawllmbanalytics.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gadgets")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Gadget {

    @Id
    private Integer id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "brawler_id")
    private Brawler brawler;
}
