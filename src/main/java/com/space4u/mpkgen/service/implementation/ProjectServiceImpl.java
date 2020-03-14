package com.space4u.mpkgen.service.implementation;

import com.space4u.mpkgen.api.request.AddProjectRequest;
import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import com.space4u.mpkgen.entity.ServiceType;
import com.space4u.mpkgen.repository.BuildingRepository;
import com.space4u.mpkgen.repository.ProjectRepository;
import com.space4u.mpkgen.repository.ServiceTypeRepository;
import com.space4u.mpkgen.service.ProjectService;
import com.space4u.mpkgen.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private BuildingRepository buildingRepository;
    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public int addProject(AddProjectRequest request) {
        Building building = buildingRepository.getOne(request.getBuildingId());
        ServiceType serviceType = serviceTypeRepository.getOne(request.getServiceTypeId());
        Project project = addProjectToDataSource(request, building,serviceType);
        return project.getId();
    }

    private Project addProjectToDataSource(AddProjectRequest request, Building building, ServiceType serviceType){
        Project project = new Project();
        project.setFloor(request.getFloor());
        project.setDate(request.getDate());
        project.setTenant(request.getTenant());
        project.setShortDescription(request.getShortDescription());
        project.setServiceType(serviceType);
        project.setProjectNum(request.getProjectNum());
        project.setMpk(request.getMpk());
        project.setBuilding(building);
        return projectRepository.save(project);
    }

    @Override
    public void updateProject(AddProjectRequest request, int id) {
        Project project = new Project();
        project.setFloor(request.getFloor());
        project.setDate(request.getDate());
        project.setTenant(request.getTenant());
        project.setShortDescription(request.getShortDescription());
        project.setServiceType(serviceTypeRepository.getOne(request.getServiceTypeId()));
        project.setProjectNum(request.getProjectNum());
        project.setMpk(request.getMpk());
        project.setBuilding(buildingRepository.getOne(request.getBuildingId()));
        project.setId(id);
        Project previousProject = projectRepository.getOne(id);
        previousProject = project;
        projectRepository.save(previousProject);
    }

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

    @Override
    public void setParameters(AddProjectRequest addProjectRequest, Project project) {
        addProjectRequest.setBuildingId(project.getBuilding().getId());
        addProjectRequest.setDate(project.getDate());
        addProjectRequest.setFloor(project.getFloor());
        addProjectRequest.setMpk(project.getMpk());
        addProjectRequest.setProjectNum(project.getProjectNum());
        addProjectRequest.setServiceTypeId(project.getServiceType().getId());
        addProjectRequest.setShortDescription(project.getShortDescription());
        addProjectRequest.setTenant(project.getTenant());
        addProjectRequest.setId(project.getId());
    }

    @Override
    public List<Project> projectsOtherThanOffer() {
        List<Project> allProjects = findAll();
        List<Project> noOfferProjects = new ArrayList<>();
        for (Project p: allProjects){
            if(p.getServiceType().getId()!=0)
                noOfferProjects.add(p);
        }
        return noOfferProjects;
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