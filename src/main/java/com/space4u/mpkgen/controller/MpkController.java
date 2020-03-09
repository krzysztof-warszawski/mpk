package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.entity.ServiceType;
import com.space4u.mpkgen.service.BuildingService;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/mpk")
public class MpkController {

    private ProjectService projectService;
    private BuildingService buildingService;
    private ServiceTypeService serviceTypeService;


    @GetMapping("/generateMPK")
    public String showGenerateMPKPage(Model model, @RequestParam("chosenBuildingId") int id){
        Building building = buildingService.getBuildingById(id);
        List<ServiceType> serviceTypeList = serviceTypeService.findAll();
        Project newProject = new Project();
        model.addAttribute("chosenBuilding", building);
        model.addAttribute("newProject", newProject);
        model.addAttribute("serviceTypeList", serviceTypeList);
        return "/mpk/generateMPK";
    }

    @RequestMapping(value="/saveMPK", method= RequestMethod.POST)
    public String saveProjectAndGenerateMPK(@ModelAttribute("newProject") Project newProject, @RequestParam("chosenBuildingId") int buildingId){
        //pobieramy  budynek na podstawie requestParam
        Building building = buildingService.getBuildingById(buildingId);
        //ustawiamy go jako budynek dla nowego projektu
        newProject.setBuilding(building);
        //pobieramy ostatni numer projektu na tym budynku i ustawiamy na tej podstawie nowy dodając "1"
        int currentProjectNr = projectService.getCurrentProjectNum(building);
       /* List<Project> projectsForThisBuilding = building.getProjects();
        projectsForThisBuilding.sort(new Comparator<Project>() {
            @Override
            public int compare(Project o1, Project o2) {
                return Integer.compare(o1.getNr_project(),o2.getNr_project());
            }
        });
        int lastProjectNr = projectsForThisBuilding.get(projectsForThisBuilding.size()-1).getNr_project();
        int currentProjectNr = lastProjectNr+1;*/
        newProject.setProjectNum(currentProjectNr);
        projectService.save(newProject);

        int currentBuildingNr = building.getBuildingNum();
        StringBuffer MPK = projectService.createMpkNumLastCharacter(currentBuildingNr,currentProjectNr);

        //pobieramy nowo zachowany projekt i z niego bierzemy typ usługi do wygenerowania MPK
        Project newProjectForData = projectService.getLastProject();
        MPK.append(newProjectForData.getServiceType().getId());
        newProject.setMpk(MPK.toString());

        //zapisujemy nasz projekt z powrotem do bazy
        projectService.save(newProject);

        //jeżeli IdServType == 5 to trzeba też dać nowy projekt o tych samych parametrach ale idServType = 6 i inne MPK
        if(newProjectForData.getServiceType().getId() == 5){
            //ustawiamy MPK dla nowego projektu - usuwamy 5 i zamieniamy na 6
            MPK.deleteCharAt(MPK.length()-1);
            MPK.append(6);
            Project newGuaranteeProject = projectService.createProjectForGuarantee(MPK, newProject, serviceTypeService);
            projectService.save(newGuaranteeProject);
        }

        return "redirect:/projects/list";
    }


}