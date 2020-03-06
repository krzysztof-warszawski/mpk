package com.space4u.mpkgen.service.implementation;

import com.space4u.mpkgen.model.Building;
import com.space4u.mpkgen.model.Project;
import com.space4u.mpkgen.repository.BuildingRepository;
import com.space4u.mpkgen.repository.ProjectRepository;
import com.space4u.mpkgen.repository.ServiceTypeRepository;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private BuildingRepository buildingRepository;
    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public List<Project> findAll() {
        List<Project> projectList = projectRepository.findAll();
        projectList.sort(Comparator.comparing(project -> project.getBuilding().getName())); // czy projects.sort(Comparator.comparing(o -> o.getBuilding().getName())); ??
        return projectList;
    }

    @Override
    public void deleteProjectById(int id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Project getProjectById(int id) {
        return projectRepository.getOne(id);
    }

    @Override
    public void save(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void createProjectForProposals(String MPK, Building building, ServiceTypeService serviceTypeService) {

    }

    @Override
    public Project createProjectForGuarantee(StringBuffer MPK, Project newProject, ServiceTypeService serviceTypeService) {
        return null;
    }

    @Override
    public StringBuffer createMpkNumLastCharacter(int currentBuildingNum, int currentProjectNum) {
        return null;
    }

    @Override
    public int getCurrentProjectNum(Building building) {
        List<Project> projectsForThisBuilding = building.getProjects();
        projectsForThisBuilding.sort(Comparator.comparingInt(Project::getProjectNum));
        return projectsForThisBuilding.get(projectsForThisBuilding.size()-1).getProjectNum()+1;
    }

    @Override
    public Project getLastProject() {
        return projectRepository.findTopByOrderByIdDesc();
    }

//    private Building extractBuildingFromRepository(int buildingId){
//        Optional<Building> optionalBuilding = buildingRepository.findById(buildingId);
//        if(!optionalBuilding.isPresent()){
//            throw new IllegalArgumentException("There is no such building in database");
//        }
//        return optionalBuilding.get();
//    }
//
//    private ServiceType extractServTypeFromRepository(int servTypeId){
//        Optional<ServiceType> optionalServType = serviceTypeRepository.findById(servTypeId);
//        if(!optionalServType.isPresent()){
//            throw new IllegalArgumentException("There is no such servType in database");
//        }
//        return optionalServType.get();
//    }
}