package de.szut.lf8_project.employee.dto.external;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class EmployeeRequestDTO
{
    private String lastName;
    private String firstName;
    private String street;
    private String postcode;
    private String city;
    private String phone;
    private Set<String> skillSet;

}

