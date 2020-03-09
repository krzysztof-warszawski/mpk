package com.space4u.mpkgen.service.implementation;

import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.entity.ServiceType;
import com.space4u.mpkgen.repository.ProjectRepository;
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
//    private BuildingRepository buildingRepository;
//    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public List<Project> findAll() {
        List<Project> projectList = projectRepository.findAll();
        projectList.sort(Comparator.comparing(project -> project.getBuilding().getName())); // czy projects.sort(Comparator.comparing(o -> o.getBuilding().getName())); ??
//        projectList.sort(Comparator.comparing(o -> o.getBuilding().getName()));
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
    public void createProjectForProposals(String MPK, Building building, ServiceTypeService serviceTypeService) { // tu mamy void <<<<<<<<<<<<<<<<<<<<<<<
        Project project = new Project();
        project.setMpk(MPK);
        project.setProjectNum(0);
        project.setBuilding(building);
        ServiceType serviceType = serviceTypeService.getServiceTypeById(0);
        project.setServiceType(serviceType);
        project.setShortDescription("OFERTOWANIE I MARKETING");
        projectRepository.save(project);
    }

    @Override
    public Project createProjectForGuarantee(StringBuffer MPK, Project newProject, ServiceTypeService serviceTypeService) { // tu mamy return <<<<<<<<<<<<<<<<<<<<<<<
        Project guaranteeProject = new Project();
        guaranteeProject.setProjectNum(newProject.getProjectNum());
        guaranteeProject.setMpk(MPK.toString());
        guaranteeProject.setBuilding(newProject.getBuilding());
        guaranteeProject.setServiceType(serviceTypeService.getServiceTypeById(6));
        guaranteeProject.setTenant(newProject.getTenant());
        guaranteeProject.setShortDescription("Gwarancja dla " + newProject.getDate() + "_" + newProject.getShortDescription());
        return guaranteeProject;
    }

    /*
    * tworzymy MPK na podstawie nr projektu i liczby znaków
    * numery budynków zaczynaja sie od 11 wiec nie bedzie krotszy niz 2 znaki
     */

    @Override
    public StringBuffer createMpkNumLastCharacter(int currentBuildingNum, int currentProjectNum) {
        StringBuffer MPK = new StringBuffer();

        if (String.valueOf(currentBuildingNum).length() == 2) {
            MPK.append("0");
        }
        MPK.append(currentBuildingNum);

        if (String.valueOf(currentProjectNum).length() == 1) {
            MPK.append("00");
        } else if (String.valueOf(currentProjectNum).length() == 2) {
            MPK.append("0");
        }
        MPK.append(currentProjectNum);

        return MPK;
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