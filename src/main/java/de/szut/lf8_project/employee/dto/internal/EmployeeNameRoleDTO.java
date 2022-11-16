package de.szut.lf8_project.employee.dto.internal;

import lombok.Data;

/**
 * Employee name role DTO.
 */
@Data
public class EmployeeNameRoleDTO {
    private Long employeeId;
    private String lastName;
    private String role;
}
