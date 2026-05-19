package com.brawllmbanalytics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tierlist_items")

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Tierlist getTierlist() {
        return tierlist;
    }

    public void setTierlist(Tierlist tierlist) {
        this.tierlist = tierlist;
    }

    public Brawler getBrawler() {
        return brawler;
    }

    public void setBrawler(Brawler brawler) {
        this.brawler = brawler;
    }

    public TierlistItem() {
        // Constructor vacío para JPA
    }

    public TierlistItem(String tier, Tierlist tierlist, Brawler brawler) {
        this.tier = tier;
        this.tierlist = tierlist;
        this.brawler = brawler;
    }
}
