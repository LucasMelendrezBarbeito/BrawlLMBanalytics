package com.brawllmbanalytics.repositories;

import com.brawllmbanalytics.entities.RankedMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RankedMatchRepository extends JpaRepository<RankedMatch, Long> {

    List<RankedMatch> findTop10ByCuentaBrawlIdOrderByBattleTimeDesc(Long cuentaBrawlId);
}
