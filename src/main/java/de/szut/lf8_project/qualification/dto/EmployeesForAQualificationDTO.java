package de.szut.lf8_project.qualification.dto;

import de.szut.lf8_project.employee.dto.external.EmployeeNameDataDTO;
import lombok.Data;

import java.util.Set;

@Data
public class EmployeesForAQualificationDTO {
    private String skill;
    private Set<EmployeeNameDataDTO> employees;
}
