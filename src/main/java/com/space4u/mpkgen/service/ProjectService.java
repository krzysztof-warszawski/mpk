package com.space4u.mpkgen.service;

import com.space4u.mpkgen.api.request.AddProjectRequest;
import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;

import java.util.List;

public interface ProjectService {

    int addProject(AddProjectRequest request);
    void updateProject(AddProjectRequest request, int id);
    List<Project> findAll();
    void deleteProjectById(int id);
    Project getProjectById(int id);
    void save(Project project);
    void createProjectForProposals(String MPK, Building building, ServiceTypeService serviceTypeService);
    Project createProjectForGuarantee(StringBuffer MPK, Project newProject, ServiceTypeService serviceTypeService);
    StringBuffer createMpkNumLastCharacter(int currentBuildingNum, int currentProjectNum);
    int getCurrentProjectNum(Building building);
    Project getLastProject();
    void setParameters(AddProjectRequest request, Project project);
    List<Project> projectsOtherThanOffer();
}
