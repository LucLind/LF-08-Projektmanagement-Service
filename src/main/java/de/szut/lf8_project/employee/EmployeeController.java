package de.szut.lf8_project.employee;

public class EmployeeController {
    private final EmployeeService service;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService service, EmployeeMapper employeeMapper){
        this.service = service;
        this.employeeMapper = employeeMapper;
    }
}
