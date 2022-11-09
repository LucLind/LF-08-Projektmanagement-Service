package de.szut.lf8_project.employee.dto;

import de.szut.lf8_project.qualification.QualificationEntity;
import lombok.Data;

import java.util.Set;

@Data
public class EmployeesForAQualificationDto
{
    private Long id;
    private String skill;
    private Set<GetEmployeeNameDto> employees;

    public EmployeesForAQualificationDto(Long id, Set<QualificationEntity> skillSet) {
        this.id = id;
        this.skill = skillSet;
    }
}
