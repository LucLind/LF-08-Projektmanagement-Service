package de.szut.lf8_project.role;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProjectEmployeeRoleRepository class
 */
public interface ProjectEmployeeRoleRepository extends JpaRepository<ProjectEmployeeRoleEntity, EmployeeRoleKey> {
}
