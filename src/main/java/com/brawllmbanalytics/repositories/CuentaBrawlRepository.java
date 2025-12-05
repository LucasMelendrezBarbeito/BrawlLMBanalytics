package com.brawllmbanalytics.repositories;

import com.brawllmbanalytics.entities.CuentaBrawl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CuentaBrawlRepository extends JpaRepository<CuentaBrawl, Integer> {
    List<CuentaBrawl> findByUsuarioId(Integer usuarioId);
    Optional<CuentaBrawl> findByTag(String tag);
}
