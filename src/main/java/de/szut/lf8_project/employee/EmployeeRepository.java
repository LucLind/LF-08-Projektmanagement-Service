package de.szut.lf8_project.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The employee repository
 */
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

}
