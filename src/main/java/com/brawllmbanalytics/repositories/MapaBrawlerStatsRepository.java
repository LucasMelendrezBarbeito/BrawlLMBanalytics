package com.brawllmbanalytics.repositories;

import com.brawllmbanalytics.entities.Mapa;
import com.brawllmbanalytics.entities.MapaBrawlerStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapaBrawlerStatsRepository extends JpaRepository<MapaBrawlerStats, Long> {

    List<MapaBrawlerStats> findTop10ByMapaOrderByWinRateDesc(Mapa mapa);
}
