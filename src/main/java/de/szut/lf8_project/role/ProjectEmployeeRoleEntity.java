package de.szut.lf8_project.role;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.project.ProjectEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ProjectEmployeeRoleEntity {
    @EmbeddedId
    EmployeeRoleKey id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_Id")
    private ProjectEntity project;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_Id")
    private EmployeeEntity employee;

    private String skill;
}
