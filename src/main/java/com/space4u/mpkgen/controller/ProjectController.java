package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.api.request.AddProjectRequest;
import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.entity.ServiceType;
import com.space4u.mpkgen.service.BuildingService;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import com.space4u.mpkgen.util.ExcelView;
import com.space4u.mpkgen.util.mappings.MpkMappings;
import com.space4u.mpkgen.util.mappings.NavMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(NavMappings.PROJECTS)
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ServiceTypeService serviceTypeService;
    @Autowired
    private BuildingService buildingService;
    private int projectId;

    @GetMapping(NavMappings.PROJECTS_LIST)
    public String listMpk(Model model) {
        model.addAttribute("projects", projectService.findAll());

        return MpkMappings.PROJECT_LIST;
    }

    @GetMapping(NavMappings.PROJECTS_EDIT)
    public String showEditProjectPage(@RequestParam(name="id") int id, Model model){
        projectId = id;
        AddProjectRequest addProjectRequest = new AddProjectRequest();
        return setParametersForEditing(model,addProjectRequest,id);
    }

    @GetMapping(NavMappings.PROJECTS_EDITABLE)
    public String showEditableProjectsPage(Model model){
        List<Project> projects = projectService.findAll();
        model.addAttribute("projects",projects);
        return MpkMappings.PROJECT_EDITABLE;
    }

    @PostMapping(NavMappings.PROJECTS_SAVE_EDITED)
    public String saveEditedProject(@ModelAttribute("addProjectRequest") @Valid AddProjectRequest request, BindingResult result,
                                    Model model){
        if(result.hasErrors()){
            // zwraca /projects/editProject
            return setParametersForEditing(model,request,projectId);
        }
        else {
            // przy edycji nie ma też automatycznej generacji projektu gwarancyjnego
            // w przypadku wybrania opcji realizacja
            // następuje wyliczanie nowego MPK
            Project oldProject = projectService.getProjectById(projectId);
            Building oldBuilding = oldProject.getBuilding();
            projectService.updateProject(request, projectId);
            Project editedProject = projectService.getProjectById(projectId);
            Building building = editedProject.getBuilding();
            ServiceType serviceType = editedProject.getServiceType();
            int currentProjectNr = projectService.getCurrentProjectNum(building);
            if (!oldBuilding.equals(building)) {
                editedProject.setProjectNum(currentProjectNr);
            }
            int currentBuildingNr = building.getBuildingNum();
            StringBuffer MPK = projectService.createMpkNumLastCharacter(currentBuildingNr, editedProject.getProjectNum());
            MPK.append(serviceType.getId());
            editedProject.setMpk(MPK.toString());
            projectService.save(editedProject);
            return "redirect:" + NavMappings.PROJECTS + NavMappings.PROJECTS_LIST;
        }
    }

    @GetMapping(NavMappings.PROJECTS_DOWNLOAD_EXCEL)
    public ModelAndView downloadExcel(Model model){
        model.addAttribute("projects", projectService.findAll());
        return new ModelAndView(new ExcelView(), "projects", projectService.findAll());
    }

    @GetMapping(NavMappings.PROJECTS_DELETE)
    public String delete(@RequestParam(name = "id") int id) {
        projectService.deleteProjectById(id);
        return "redirect:" + NavMappings.PROJECTS + NavMappings.PROJECTS_LIST;
    }


    private String setParametersForEditing(Model model, AddProjectRequest request, int projectId){
        Project project = projectService.getProjectById(projectId);
        projectService.setParameters(request, project);
        List<Building> buildingList = buildingService.findAll();
        List<ServiceType> serviceTypeList = serviceTypeService.findAll();
        model.addAttribute("buildingList",buildingList);
        model.addAttribute("serviceTypeList", serviceTypeList);
        model.addAttribute("addProjectRequest", request);
        return MpkMappings.PROJECT_EDIT;
    }
}
