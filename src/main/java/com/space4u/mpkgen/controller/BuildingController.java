package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.service.BuildingService;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/buildings")
public class BuildingController {

    private BuildingService buildingService;
    private ProjectService projectService;
    private ServiceTypeService serviceTypeService;

    @RequestMapping("/building_choose")
    public String viewBuildingListPage(Model model){
        List<Building> buildingList = buildingService.findAll();
        model.addAttribute("buildings", buildingList);
        return "/buildings/building_choose";
    }

    @GetMapping("/chooseBuilding")
    public String showBuildingProjectsPage(Model model, @RequestParam("buildingId") int id){
        Building building = buildingService.getBuildingById(id);
        List<Project> projectsForBuilding = building.getProjects();
        model.addAttribute("chosenBuilding", building);
        model.addAttribute("projectsForBuilding", projectsForBuilding);
        return "/buildings/building_projects";
    }

    @RequestMapping("/addNewBuilding")
    public String showNewBuildingPage(Model model){
        Building building = new Building();
        model.addAttribute("building", building);
        return "/buildings/new_building";
    }

    @RequestMapping(value="/saveBuilding", method= RequestMethod.POST)
    public String saveBuildingAndProject(@ModelAttribute("building") Building building){
        int buildingNum = buildingService.getLastBuildingNum()+1;
        building.setBuildingNum(buildingNum);
        buildingService.save(building);
        //Building currentBuilding = buildingService.findBuildingByName(building.getName());
        Building currentBuilding = buildingService.getLastBuilding();
        String currentBuildingNr = String.valueOf(currentBuilding.getBuildingNum());
        String MPK;
        if(currentBuildingNr.length()==2)
            MPK = "0"+currentBuildingNr+"0000";
        else
            MPK=currentBuildingNr+"0000";
        projectService.createProjectForProposals(MPK,currentBuilding, serviceTypeService);

        return "redirect:/projects/list";
    }

    @GetMapping("/buildingsList")
    public String showAllBuildings(Model model){
        List<Building> buildings = buildingService.findAll();
        buildings.sort(Comparator.comparing(building -> building.getName()));
        model.addAttribute(buildings);
        return "/buildings/list-buildings";
    }
}