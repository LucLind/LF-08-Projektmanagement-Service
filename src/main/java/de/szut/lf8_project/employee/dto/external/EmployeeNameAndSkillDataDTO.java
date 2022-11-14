package de.szut.lf8_project.employee.dto.external;

import de.szut.lf8_project.qualification.dto.QualificationDto;
import lombok.Data;

import java.util.Set;

@Data
public class EmployeeNameAndSkillDataDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private Set<QualificationDto> skillSet;
}
