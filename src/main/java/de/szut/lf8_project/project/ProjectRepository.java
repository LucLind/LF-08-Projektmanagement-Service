package de.szut.lf8_project.project;

import de.szut.lf8_project.project.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    //List<ProjectEntity> findByEmployeesId(Long employeeId);
}
