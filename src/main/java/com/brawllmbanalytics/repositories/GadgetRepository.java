package com.brawllmbanalytics.repositories;

import com.brawllmbanalytics.entities.Gadget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GadgetRepository extends JpaRepository<Gadget, Integer> {
}
