package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.service.BuildingService;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import com.space4u.mpkgen.util.mappings.MpkMappings;
import com.space4u.mpkgen.util.mappings.NavMappings;
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
@RequestMapping(NavMappings.BUILDINGS)
public class BuildingController {

    private BuildingService buildingService;
    private ProjectService projectService;
    private ServiceTypeService serviceTypeService;

    @RequestMapping(NavMappings.BUILDINGS_CHOOSE)
    public String viewBuildingListPage(Model model){
        List<Building> buildingList = buildingService.findAll();
        model.addAttribute("buildings", buildingList);
        return MpkMappings.BUILDING_CHOOSE;
    }

    @GetMapping(NavMappings.BUILDINGS_CHOOSE_ONE)
    public String showBuildingProjectsPage(Model model, @RequestParam("buildingId") int id){
        if (id < 11) {
            return "redirect:" + MpkMappings.BUILDING_CHOOSE;
        } else {
            Building building = buildingService.getBuildingById(id);
            List<Project> projectsForBuilding = building.getProjects();
            model.addAttribute("chosenBuilding", building);
            model.addAttribute("projectsForBuilding", projectsForBuilding);
            return MpkMappings.BUILDING_PROJECTS;
        }
    }

    @RequestMapping(NavMappings.BUILDINGS_NEW)
    public String showNewBuildingPage(Model model){
        Building building = new Building();
        model.addAttribute("building", building);
        return MpkMappings.BUILDING_NEW;
    }

    @RequestMapping(value=NavMappings.BUILDINGS_SAVE, method= RequestMethod.POST)
    public String saveBuildingAndProject(@ModelAttribute("building") @Valid Building building, BindingResult result){
        if(result.hasErrors()){
            return MpkMappings.BUILDING_NEW;
        }
        else {
            boolean buildingExists=false;
            //sprawdzanie czy budynek o danej nazwie już istnieje w bazie
            for (Building b:buildingService.findAll()
            ) {
                if(b.getName().equalsIgnoreCase(building.getName())){
                    buildingExists = true;
                }}
            if(buildingExists){
                return MpkMappings.BUILDING_NEW_EXISTS;
            }
            else{
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
                return "redirect:" + NavMappings.PROJECTS + NavMappings.PROJECTS_LIST;
            }
        }
    }

    @GetMapping(NavMappings.BUILDINGS_LIST)
    public String showOnlyOffersBuildings(Model model){
        List<Building> buildings = buildingService.onlyOfferBuildings();
        buildings.sort(Comparator.comparing(Building::getName, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute(buildings);
        return MpkMappings.BUILDING_LIST;
    }

    @GetMapping(NavMappings.BUILDINGS_DELETE)
    public String delete(@RequestParam(name = "id") int id) {
        //na podstawie id budynku pobieram id projektu ofertowego i go usuwam
        Building building = buildingService.getBuildingById(id);
        List<Project> projectsOnBuilding = building.getProjects();
        if(projectsOnBuilding.size()>0) {
            int idProject = projectsOnBuilding.get(0).getId();
            projectService.deleteProjectById(idProject);
        }
        buildingService.deleteBuildingById(id);
        return "redirect:" + NavMappings.BUILDINGS + NavMappings.BUILDINGS_LIST;
    }

    @GetMapping(NavMappings.BUILDINGS_EDIT)
    public ModelAndView showEditBuildingPage(@RequestParam(name = "id") int id) {
        ModelAndView mav = new ModelAndView(MpkMappings.BUILDING_EDIT);
        Building building = buildingService.getBuildingById(id);
        mav.addObject("building", building);
        return mav;
    }

    @RequestMapping(value=NavMappings.BUILDINGS_SAVE_EDIT, method= RequestMethod.POST)
    public String saveEditedBuilding(@ModelAttribute("building") @Valid Building building, BindingResult result,
                                     Model model){
        if(result.hasErrors()){
            model.addAttribute("building", building);
            return MpkMappings.BUILDING_EDIT;
        }
        else {
            buildingService.save(building);
            return "redirect:"+NavMappings.BUILDINGS + NavMappings.BUILDINGS_LIST;
        }
    }
}