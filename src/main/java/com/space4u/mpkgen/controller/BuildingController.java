package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.service.BuildingService;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
        if (id < 11) {
            return "redirect:/buildings/building_choose";
        } else {
            Building building = buildingService.getBuildingById(id);
            List<Project> projectsForBuilding = building.getProjects();
            model.addAttribute("chosenBuilding", building);
            model.addAttribute("projectsForBuilding", projectsForBuilding);
            return "/buildings/building_projects";
        }
    }

    @RequestMapping("/addNewBuilding")
    public String showNewBuildingPage(Model model){
        Building building = new Building();
        model.addAttribute("building", building);
        return "/buildings/new_building";
    }

    @RequestMapping(value="/saveBuilding", method= RequestMethod.POST)
    public String saveBuildingAndProject(@ModelAttribute("building") @Valid Building building, BindingResult result){
        if(result.hasErrors()){
            return "/buildings/new_building";
        }
        else {
            int buildingNum = buildingService.getLastBuildingNum() + 1;
            building.setBuildingNum(buildingNum);
            buildingService.save(building);
            Building currentBuilding = buildingService.getLastBuilding();
            String currentBuildingNr = String.valueOf(currentBuilding.getBuildingNum());
            String MPK;
            if (currentBuildingNr.length() == 2)
                MPK = "0" + currentBuildingNr + "0000";
            else
                MPK = currentBuildingNr + "0000";
            projectService.createProjectForProposals(MPK, currentBuilding, serviceTypeService);
            return "redirect:/projects/list";
        }
    }

    @GetMapping("/buildingsList")
    public String showOnlyOffersBuildings(Model model){
        List<Building> buildings = buildingService.onlyOfferBuildings();
        buildings.sort(Comparator.comparing(Building::getName));
        model.addAttribute(buildings);
        return "/buildings/list-buildings";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(name = "id") int id) {
        //na podstawie id budynku pobieram id projektu ofertowego i go usuwam
        Building building = buildingService.getBuildingById(id);
        List<Project> projectsOnBuilding = building.getProjects();
        int idProject = projectsOnBuilding.get(0).getId();
        projectService.deleteProjectById(idProject);
        buildingService.deleteBuildingById(id);
        return "redirect:/buildings/buildingsList";
    }

    @GetMapping("/edit")
    public ModelAndView showEditBuildingPage(@RequestParam(name = "id") int id) {
        ModelAndView mav = new ModelAndView("/buildings/edit_building");
        Building building = buildingService.getBuildingById(id);
        mav.addObject("building", building);
        return mav;
    }

    @RequestMapping(value="/saveEditedBuilding", method= RequestMethod.POST)
    public String saveEditedBuilding(@ModelAttribute("building") @Valid Building building, BindingResult result,
                                     Model model){
        if(result.hasErrors()){
            model.addAttribute("building", building);
            return "/buildings/edit_building";
        }
        else {
            buildingService.save(building);
            return "redirect:/buildings/buildingsList";
        }
    }
}