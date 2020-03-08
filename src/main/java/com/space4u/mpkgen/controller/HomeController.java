package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

//    private BuildingService buildingService;
//    private EmployeeService employeeService;
    private ProjectService projectService;
//    private ServiceTypeService serviceTypeService;


    @GetMapping("/mpkList")
    public String viewHomePage(Model model) {
        model.addAttribute("projects", projectService.findAll());
        System.out.println("Jestem tu");
        return "mpkList";
    }
}
