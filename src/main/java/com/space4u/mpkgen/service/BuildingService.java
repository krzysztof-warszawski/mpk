package com.space4u.mpkgen.service;

import com.space4u.mpkgen.entity.Building;

import java.util.List;

public interface BuildingService {

    List<Building> findAll();
    void deleteBuildingById(int id);
    Building getBuildingById(int id);
    Building findBuildingByName(String name);
    void save(Building building);
    int getLastBuildingNum();
    Building getLastBuilding();
    List<Building> onlyOfferBuildings();
}
