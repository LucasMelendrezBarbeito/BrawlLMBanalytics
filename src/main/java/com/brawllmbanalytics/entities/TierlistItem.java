package com.brawllmbanalytics.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tierlist_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TierlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tier;

    @ManyToOne
    @JoinColumn(name = "tierlist_id")
    private Tierlist tierlist;

    @ManyToOne
    @JoinColumn(name = "brawler_id")
    private Brawler brawler;
}
