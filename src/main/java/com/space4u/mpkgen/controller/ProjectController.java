package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.service.BuildingService;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import com.space4u.mpkgen.util.ExcelView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService projectService;
    private ServiceTypeService serviceTypeService;
    private BuildingService buildingService;
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
        model.addAttribute("project", project);
        return "/projects/edit_project";
    }

    @PostMapping("/saveEditedProject")
    public String saveEditedProject(@ModelAttribute("project") Project project){
        projectService.save(project);
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