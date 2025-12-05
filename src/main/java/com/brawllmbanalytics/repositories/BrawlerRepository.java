package com.brawllmbanalytics.repositories;

import com.brawllmbanalytics.entities.Brawler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrawlerRepository extends JpaRepository<Brawler, Integer> {
}
