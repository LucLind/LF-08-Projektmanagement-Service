package de.szut.lf8_project.employee.dto;

import de.szut.lf8_project.qualification.dto.GetQualificationDto;
import lombok.Data;

import java.util.Set;

@Data
public class EmployeesForAQualificationDto
{
    private String skill;
    private Set<GetEmployeeNameDto> employees;
}
