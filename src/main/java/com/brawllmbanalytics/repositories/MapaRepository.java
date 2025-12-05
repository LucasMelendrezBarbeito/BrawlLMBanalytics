package com.brawllmbanalytics.repositories;

import com.brawllmbanalytics.entities.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapaRepository extends JpaRepository<Mapa, Long> {

    // Mapas marcados manualmente como en rotación (si algún día los usas)
    List<Mapa> findByEnRotacionTrueOrderByModoAscNombreAsc();

    // Todos los mapas ordenados
    List<Mapa> findAllByOrderByModoAscNombreAsc();

    // 🔥 NUEVO: Buscar por ID real de Supercell
    Mapa findBySupercellId(Long supercellId);
}
