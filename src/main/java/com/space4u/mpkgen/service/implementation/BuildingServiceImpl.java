package com.space4u.mpkgen.service.implementation;

import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.repository.BuildingRepository;
import com.space4u.mpkgen.service.BuildingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private BuildingRepository buildingRepository;

    @Override
    public List<Building> findAll() {
        List<Building> buildingList = buildingRepository.findAll();
        buildingList.sort(Comparator.comparing(Building::getName, String.CASE_INSENSITIVE_ORDER));
        return buildingList;
    }

    @Override
    public void deleteBuildingById(int id) {
        buildingRepository.deleteById(id);
    }

    @Override
    public Building getBuildingById(int id) {
        return buildingRepository.getOne(id);
    }

    @Override
    public Building findBuildingByName(String name) {
        return buildingRepository.findBuildingByName(name);
    }

    @Override
    public void save(Building building) {
        buildingRepository.save(building);
    }

    @Override
    public int getLastBuildingNum() {
        return buildingRepository.findTopByOrderByBuildingNumDesc().getBuildingNum();
    }

    @Override
    public Building getLastBuilding() {
        return buildingRepository.findTopByOrderByIdDesc();
    }

    //lista, w której są budynki, dla których instnieje tylko projekt ofertowy
    //potrzebny przy edytuj/usuń budynek
    @Override
    public List<Building> onlyOfferBuildings() {
        List<Building> buildings = new ArrayList<>();
        for (Building b: findAll()) {
            if (b.getProjects().size() == 1)
                buildings.add(b); }
        return buildings;
    }
}