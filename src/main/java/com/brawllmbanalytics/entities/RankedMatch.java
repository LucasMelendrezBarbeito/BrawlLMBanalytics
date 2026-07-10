package com.brawllmbanalytics.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ranked_matches")
public class RankedMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_brawl_id", nullable = false)
    private CuentaBrawl cuentaBrawl;

    @Column(name = "battle_time", nullable = false)
    private LocalDateTime battleTime;

    @Column(nullable = false)
    private String mode;

    private Integer rank;

    @Column(name = "trophy_change")
    private Integer trophyChange;

    @Column(name = "brawler_name", nullable = false)
    private String brawlerName;

    @Column(name = "brawler_id", nullable = false)
    private Integer brawlerId;

    private String result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CuentaBrawl getCuentaBrawl() {
        return cuentaBrawl;
    }

    public void setCuentaBrawl(CuentaBrawl cuentaBrawl) {
        this.cuentaBrawl = cuentaBrawl;
    }

    public LocalDateTime getBattleTime() {
        return battleTime;
    }

    public void setBattleTime(LocalDateTime battleTime) {
        this.battleTime = battleTime;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getTrophyChange() {
        return trophyChange;
    }

    public void setTrophyChange(Integer trophyChange) {
        this.trophyChange = trophyChange;
    }

    public String getBrawlerName() {
        return brawlerName;
    }

    public void setBrawlerName(String brawlerName) {
        this.brawlerName = brawlerName;
    }

    public Integer getBrawlerId() {
        return brawlerId;
    }

    public void setBrawlerId(Integer brawlerId) {
        this.brawlerId = brawlerId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
