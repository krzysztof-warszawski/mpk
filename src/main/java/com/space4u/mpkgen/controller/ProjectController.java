package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.api.request.AddProjectRequest;
import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.entity.ServiceType;
import com.space4u.mpkgen.service.BuildingService;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import com.space4u.mpkgen.util.ExcelView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
//@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ServiceTypeService serviceTypeService;
    @Autowired
    private BuildingService buildingService;
    private int projectId;
//    private EmployeeService employeeService;


    @GetMapping("/list")
    public String listMpk(Model model) {
        model.addAttribute("projects", projectService.findAll());

        return "/projects/list-projects";
    }

//    @GetMapping("/edit")
//    public ModelAndView showEditProjectPage(@RequestParam(name="id") int id, Model model){
//        ModelAndView mav = new ModelAndView("projects/edit_project");
//        Project project = projectService.getProjectById(id);
//        mav.addObject("project", project);
//        model.addAttribute("serviceTypeList", serviceTypeService.findAll());
//        return mav;
//    }

    @GetMapping("/edit")
    public String showEditProjectPage(@RequestParam(name="id") int id, Model model){
        Project project = projectService.getProjectById(id);
        AddProjectRequest addProjectRequest = new AddProjectRequest();
        /*addProjectRequest.setBuildingId(project.getBuilding().getId());
        addProjectRequest.setDate(project.getDate());
        addProjectRequest.setFloor(project.getFloor());
        addProjectRequest.setMpk(project.getMpk());
        addProjectRequest.setProjectNum(project.getProjectNum());
        addProjectRequest.setServiceTypeId(project.getServiceType().getId());
        addProjectRequest.setShortDescription(project.getShortDescription());
        addProjectRequest.setTenant(project.getTenant());
        addProjectRequest.setId(project.getId());*/
        projectService.setParameters(addProjectRequest,project);
        List<Building> buildingList = buildingService.findAll();
        List<ServiceType> serviceTypeList = serviceTypeService.findAll();
        projectId = id;
        model.addAttribute("buildingList",buildingList);
        model.addAttribute("serviceTypeList", serviceTypeList);
        model.addAttribute("addProjectRequest", addProjectRequest);
        return "/projects/edit_project";
    }

    @PostMapping("/saveEditedProject")
    public String saveEditedProject(@ModelAttribute("addProjectRequest") AddProjectRequest request){
        //nie powinno być opcji wyboru kalkulacji kosztowej - to powinno się dodawać tylko przy dodawaniu budynku
        //przy edycji nie ma też automatycznej generacji projektu gwarancyjnego
        projectService.updateProject(request,projectId);
        Project editedProject = projectService.getProjectById(projectId);
        Building building = editedProject.getBuilding();
        ServiceType serviceType = editedProject.getServiceType();
        int currentProjectNr = projectService.getCurrentProjectNum(building);
        editedProject.setProjectNum(currentProjectNr);
        //projectService.save(project);
        int currentBuildingNr = building.getBuildingNum();
        StringBuffer MPK = projectService.createMpkNumLastCharacter(currentBuildingNr,currentProjectNr);
        MPK.append(serviceType.getId());
        editedProject.setMpk(MPK.toString());
        projectService.save(editedProject);
        return "redirect:/projects/list";
    }

    @GetMapping("/downloadExcel")
    public ModelAndView downloadExcel(Model model){
        model.addAttribute("projects", projectService.findAll());
        return new ModelAndView(new ExcelView(), "projects", projectService.findAll());
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(name = "id") int id) {
        projectService.deleteProjectById(id);
        return "redirect:/projects/list";
    }


}