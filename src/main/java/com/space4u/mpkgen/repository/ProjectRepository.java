package com.space4u.mpkgen.repository;

import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findAllByBuilding(Building building);
    Project findTopByOrderByIdDesc();
}
