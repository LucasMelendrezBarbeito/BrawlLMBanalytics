package com.brawllmbanalytics.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "brawlers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Brawler {

    @Id
    private Integer id;

    private String nombre;
    private String rareza;
    private String descripcion;

    @OneToMany(mappedBy = "brawler", cascade = CascadeType.ALL)
    private List<Gadget> gadgets;

    @OneToMany(mappedBy = "brawler", cascade = CascadeType.ALL)
    private List<StarPower> starPowers;

    @Transient
    private String iconUrl;

@PostLoad
public void generarIconUrl() {
    this.iconUrl = "https://cdn.brawlify.com/brawlers/borderless/" + this.id + ".png";
}

}