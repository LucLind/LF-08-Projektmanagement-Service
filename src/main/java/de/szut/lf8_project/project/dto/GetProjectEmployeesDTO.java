package de.szut.lf8_project.project.dto;

import de.szut.lf8_project.employee.dto.internal.EmployeeNameRoleDTO;
import lombok.Data;

import java.util.Set;

/**
 * GetProjectEmployeesDTO class
 */
@Data
public class GetProjectEmployeesDTO {
    private Long projectId;
    private String description;
    private EmployeeNameRoleDTO mainEmployee;
    private Set<EmployeeNameRoleDTO> employees;
}
