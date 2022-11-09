package de.szut.lf8_project.employee.dto;

import de.szut.lf8_project.qualification.QualificationEntity;
import lombok.Data;

import java.util.Set;

@Data
public class GetEmployeeDto
{
    private Long id;
    private String lastName;
    private String firstName;
    private String street;
    private String postcode;
    private String city;
    private String phone;
    private Set<String> skillSet;

    public GetEmployeeDto(Long id) {
        this.id = id;
    }
}

