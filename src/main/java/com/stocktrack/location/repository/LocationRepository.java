package com.stocktrack.location.repository;

import com.stocktrack.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByCorridorAndShelfAndDrawer(String corridor, String shelf, String drawer);

    boolean existsByCorridorAndShelfAndDrawerAndIdNot(String corridor, String shelf, String drawer, Long id);
}
