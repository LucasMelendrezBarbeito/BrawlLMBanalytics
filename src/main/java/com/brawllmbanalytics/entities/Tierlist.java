package com.brawllmbanalytics.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "tierlists")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Tierlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"tierlists", "resenas"})
    private Usuario usuario;

    @OneToMany(mappedBy = "tierlist", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"tierlist"}) // NO ignorar usuario
    private List<TierlistItem> items;

    @OneToMany(mappedBy = "tierlist", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"tierlist"}) // IMPORTANTE: NO ignorar usuario aquí
    private List<ResenaTierlist> reviews;
}

