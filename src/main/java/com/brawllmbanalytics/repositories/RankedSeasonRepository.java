package com.brawllmbanalytics.repositories;

import com.brawllmbanalytics.entities.RankedSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RankedSeasonRepository extends JpaRepository<RankedSeason, Long> {

    List<RankedSeason> findByCuentaBrawlIdOrderBySeasonNumberDesc(Long cuentaBrawlId);

    Optional<RankedSeason> findByCuentaBrawlIdAndSeasonNumber(Long cuentaBrawlId, Integer seasonNumber);
}
