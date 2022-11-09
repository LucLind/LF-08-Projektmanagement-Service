package de.szut.lf8_project.employee.dto;

import lombok.Data;

@Data
public class GetEmployeeNameDto
{
    private Long id;
    private String lastName;
    private String firstName;
}
