package de.szut.lf8_project.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The project repository interface
 */
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByMainEmployee(Long mainEmployee);
}
