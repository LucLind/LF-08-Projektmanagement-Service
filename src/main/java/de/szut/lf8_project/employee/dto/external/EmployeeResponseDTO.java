package de.szut.lf8_project.employee.dto.external;

import lombok.Data;

import java.util.Set;

/**
 * Employee response DTO class.
 */
@Data
public class EmployeeResponseDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String street;
    private String postcode;
    private String city;
    private String phone;
    private Set<String> skillSet;
}
