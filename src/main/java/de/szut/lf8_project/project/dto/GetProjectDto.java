package de.szut.lf8_project.project.dto;

import de.szut.lf8_project.employee.dto.external.EmployeeNameAndSkillDataDTO;
import de.szut.lf8_project.employee.dto.external.EmployeeNameDataDTO;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class GetProjectDto {
    private Long id;
    private String description;
    private String comment;
    private Date startDate;
    private Date estimatedEndDate;
    private Date finalEndDate;
    private Long mainEmployee;
    private Set<Long> employees;
    private Long customer;
}
