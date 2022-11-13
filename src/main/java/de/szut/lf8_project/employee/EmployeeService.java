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
                    .map(d -> EmployeeMapper.EmployeeResponseDTOToEmployeeManagementEntity(d))
                    .collect(Collectors.toSet());
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return employee.stream().map(e -> getEmployeeEntity(e)).collect(Collectors.toSet());
    }

    public List<EmployeeEntity> readByProjectId(Long projectId){
        // TODO
        return null;
    }


    /**
     * Liest aus aud der Datenbank und valdiert mit Mitarbeiter dienst
     * @param employeeIds Die IDs der Mitarbeiter
     * @return Mitarbeiter mit gewählten IDs
     */
    public Set<Employee> readById(Set<Long> employeeIds, String token) {

        var output = new HashSet<Employee>();

        for (Long id : employeeIds) {
            output.add(readById(id, token));
        }

        return output;
    }

    /**
     * Liest aus aud der datenbank und valdiert mit mitarbeiter dienst
     * @param id
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

            employee = EmployeeMapper.EmployeeResponseDTOToEmployeeManagementEntity(externalDto);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return getEmployeeEntity(employee);
    }

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

    public EmployeeEntity update(EmployeeEntity entity){
        return repository.save(entity);
    }

    public boolean employeeIsValid(Long id){
        // TODO check if the employee is valid
        return false;
    }


    public boolean employeeHasSkill(Long id,String skill){
        // TODO check if the employee has the qualification
        return true;
    }


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
