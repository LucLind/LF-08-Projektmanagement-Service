package de.szut.lf8_project.employee;

import de.szut.lf8_project.employee.dto.external.EmployeeResponseDTO;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.helper.JsonHelper;
import de.szut.lf8_project.project.ProjectEntity;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.qualification.dto.EmployeesForAQualificationDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private static final String EMPLOYEE_SERVICE_URL = "https://employee.szut.dev/employees";
    private static final String QUALIFICATION_SERVICE_URL = "https://employee.szut.dev/qualifications";
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository){
        this.repository = repository;
    }

    /**
     * reads all employees out of the service.
     * DEPRECATED and no longer in use!!!
     * @param token the authorization token
     * @return set of employees
     */
    public Set<Employee> readAll(String token){
        EmployeeResponseDTO[] externalDto;
        Set<Employee> employee;
        try
        {
            var url = new URL(EMPLOYEE_SERVICE_URL);
            HttpURLConnection MyConn = (HttpURLConnection) url.openConnection();
            MyConn.setRequestMethod(HttpMethod.GET.name());
            MyConn.setRequestProperty(HttpHeaders.AUTHORIZATION, token);

            externalDto = JsonHelper.getDTOFromConnection(EmployeeResponseDTO[].class, MyConn);

            if(externalDto == null){
                throw new ResourceNotFoundException("Could not find employees");
            }

            employee = Arrays.stream(externalDto)
                    .map(d -> EmployeeMapper.EmployeeResponseDTOToEmployee(d))
                    .collect(Collectors.toSet());
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return employee.stream().map(e -> getEmployeeEntity(e)).collect(Collectors.toSet());
    }


    /**
     * reads employees from the database and validates with employee service
     * @param employeeIds the ids of the employees
     * @param token the authorization token
     * @return employees with its ids
     */
    public Set<Employee> readById(Set<Long> employeeIds, String token) {

        var output = new HashSet<Employee>();

        for (Long id : employeeIds) {
            output.add(readById(id, token));
        }

        return output;
    }

    /**
     * reads employee from the database and validates with employee service
     * @param id employee id
     * @param token the authorization token
     * @return
     */
    public Employee readById(Long id, String token) {
        EmployeeResponseDTO externalDto;
        Employee employee;
        try
        {
            var url = new URL(EMPLOYEE_SERVICE_URL + "/" + id);
            HttpURLConnection MyConn = (HttpURLConnection) url.openConnection();
            MyConn.setRequestMethod(HttpMethod.GET.name());
            MyConn.setRequestProperty(HttpHeaders.AUTHORIZATION, token);

            externalDto = JsonHelper.getDTOFromConnection(EmployeeResponseDTO.class, MyConn);

            if(externalDto == null){
                throw new ResourceNotFoundException("Could not find employee with Id: " + id);
            }

            employee = EmployeeMapper.EmployeeResponseDTOToEmployee(externalDto);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return getEmployeeEntity(employee);
    }

    /**
     * reads employees by their qualifications
     * DEPRECATED and no longer in use!!!
     * @param skill the qualification
     * @param token the authorization token
     * @return set of employees
     */
    public Set<Employee> readBySkill(String skill, String token) {
        EmployeesForAQualificationDTO externalDto;
        Set<Employee> employees;

        try
        {
            var url = new URL(QUALIFICATION_SERVICE_URL + "/" + skill + "/employees");
            HttpURLConnection MyConn = (HttpURLConnection) url.openConnection();
            MyConn.setRequestMethod(HttpMethod.GET.name());
            MyConn.setRequestProperty(HttpHeaders.AUTHORIZATION, token);

            externalDto = JsonHelper.getDTOFromConnection(EmployeesForAQualificationDTO.class, MyConn);

            if(externalDto == null){
                throw new ResourceNotFoundException("Could not find employees with Skill: " + skill);
            }


            employees = EmployeeMapper.employeesForAQualificationDTOToEmployee(externalDto);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return employees.stream().map(e -> getEmployeeEntity(e)).collect(Collectors.toSet());
    }

    /**
     * updates the employee entity
     * DEPRECATED and no longer in use!!!
     * @param entity the employee entity
     * @return updated employee entity
     */
    public EmployeeEntity update(EmployeeEntity entity){
        return repository.save(entity);
    }

    /**
     * checks wether the employee is free or not int the project time.
     * @param employee the employee
     * @param dto the AddProjectDto
     * @return true, if free, false if busy
     */
    public boolean employeeIsFree(Employee employee, AddProjectDto dto){
        if (employee.getEntity().getMainProject() == null || employee.getEntity().getMainProject().isEmpty()){
            return true;
        }

        for (ProjectEntity project : employee.getEntity().getMainProject()) {
            Date startDateEmployee = project.getStartDate();
            Date estEndDateEmployee = project.getEstimatedEndDate();

            Date startDateProject = dto.getStartDate();
            Date estEndDateProject = dto.getEstimatedEndDate();


            if(startDateEmployee.compareTo(startDateProject)<0 && estEndDateEmployee.compareTo(estEndDateProject)>0 ||
                    startDateEmployee.compareTo(startDateProject)>0 && estEndDateEmployee.compareTo(estEndDateProject)<0 ||
                    startDateEmployee.compareTo(startDateProject) == 0 )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * checks wether the employee is free or not int the project time.
     * @param employee the employee
     * @param requestedProject the project
     * @return true, if free, false if busy
     */
    public boolean employeeIsFree(Employee employee, ProjectEntity requestedProject){
        if (employee.getEntity().getMainProject() == null || employee.getEntity().getMainProject().isEmpty()){
            return true;
        }

        for (ProjectEntity project : employee.getEntity().getMainProject()) {
            Date startDateEmployee = project.getStartDate();
            Date estEndDateEmployee = project.getEstimatedEndDate();

            Date startDateProject = requestedProject.getStartDate();
            Date estEndDateProject = requestedProject.getEstimatedEndDate();


            if(startDateEmployee.compareTo(startDateProject)<0 && estEndDateEmployee.compareTo(estEndDateProject)>0 ||
                    startDateEmployee.compareTo(startDateProject)>0 && estEndDateEmployee.compareTo(estEndDateProject)<0 ||
                    startDateEmployee.compareTo(startDateProject) == 0 )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Getter for the employee entity
     * @param employee the employee
     * @return the employee
     */
    private Employee getEmployeeEntity(Employee employee){
        Optional<EmployeeEntity> optional = repository.findById(employee.getId());
        EmployeeEntity employeeProjectEntity;

        if (optional.isEmpty()){
            employeeProjectEntity = new EmployeeEntity(employee);
            employeeProjectEntity = repository.saveAndFlush(employeeProjectEntity);
            employee.setEntity(employeeProjectEntity);
        }
        else {
            employeeProjectEntity = optional.get();
            employee.setEntity(employeeProjectEntity);
        }

        return employee;
    }

}
