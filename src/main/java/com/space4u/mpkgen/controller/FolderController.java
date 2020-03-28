package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.util.FolderCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/folders")
public class FolderController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/createFolders")
    public String createFolders(@RequestParam(name = "id") int projectId, Model model) {
        Project project = projectService.getProjectById(projectId);
        int serviceTypeId = project.getServiceType().getId();

        FolderCreator folderCreator = new FolderCreator();
        folderCreator.setBuildingNum(project.getBuilding().getBuildingNum());
        folderCreator.setBuildingName(project.getBuilding().getName());

        try {
            if (serviceTypeId != 0) {
                folderCreator.setMpk(project.getMpk());
                folderCreator.setDate(project.getDate());
                folderCreator.setFloor(project.getFloor());
                folderCreator.setTenant(project.getTenant());
                folderCreator.createFolders();

                if (serviceTypeId == 5) {
                    project = projectService.getProjectById(projectId + 1);
                    folderCreator.setMpk(project.getMpk());
                    folderCreator.createFolders();
                }

                return "/folders/add-folders-confirmation";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "/folders/add-folders-rejection";
        }

        String currDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        folderCreator.setDate(currDate);
        model.addAttribute("folderCreator", folderCreator);
        return "/folders/add-folders";
    }

    @PostMapping("/saveFolders")
    public String saveFolders(@ModelAttribute(name = "folderCreator") FolderCreator folder) {
        try {
            folder.createOfferFolders();
            return "/folders/add-folders-confirmation";
        } catch (IOException e) {
            e.printStackTrace();
            return "/folders/add-folders-rejection";
        }
    }

}