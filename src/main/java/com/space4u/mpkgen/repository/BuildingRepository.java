package com.space4u.mpkgen.repository;

import com.space4u.mpkgen.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Integer> {

    Building findBuildingByName(String name);
    Building findTopByOrderByBuildingNumDesc();
    Building findTopByOrderByIdDesc();
}
