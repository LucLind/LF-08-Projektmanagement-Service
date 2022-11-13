package de.szut.lf8_project.employee;

import lombok.Data;

import java.util.Set;

@Data
public class Employee {

    private Long id;
    private String firstName;
    private String lastName;
    private String street;
    private String postcode;
    private String city;
    private String phone;
    private Set<String> skillSet;
    private EmployeeEntity entity;

}
