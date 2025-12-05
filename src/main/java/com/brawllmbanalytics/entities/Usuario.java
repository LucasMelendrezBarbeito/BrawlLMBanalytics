package com.brawllmbanalytics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;

    @Column(nullable = false)
    private String rol = "USER";

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CuentaBrawl> cuentasBrawl;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"usuario", "reviews", "items"})
    private List<Tierlist> tierlists;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"usuario", "tierlist"})
    private List<ResenaTierlist> resenas;
}
