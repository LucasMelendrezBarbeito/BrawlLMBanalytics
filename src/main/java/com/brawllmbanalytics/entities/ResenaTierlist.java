package com.brawllmbanalytics.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resenas_tierlist")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ResenaTierlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

 
    private Integer puntuacion;

    private String comentario;

    @ManyToOne
    @JoinColumn(name = "tierlist_id")
    @JsonIgnoreProperties({"reviews", "items"})
    private Tierlist tierlist;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"tierlists", "resenas"})
    private Usuario usuario;
}
