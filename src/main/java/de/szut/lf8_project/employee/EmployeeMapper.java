package de.szut.lf8_project.employee;

import de.szut.lf8_project.employee.dto.external.EmployeeNameAndSkillDataDTO;
import de.szut.lf8_project.employee.dto.external.EmployeeRequestDTO;
import de.szut.lf8_project.employee.dto.external.EmployeeResponseDTO;
import de.szut.lf8_project.qualification.dto.EmployeesForAQualificationDTO;
import de.szut.lf8_project.qualification.dto.QualificationDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Employee mapper class.
 */
@Service
public class EmployeeMapper {

    /**
     * forms a employee entity out of the employee dto
     * @param dto the employee response dto
     * @return the employee entity
     */
    public static EmployeeEntity GetEmployeeDtoToEmployeeEntity(EmployeeResponseDTO dto){
        var entity = new EmployeeEntity();
        entity.setId(dto.getId());

        return entity;
    }

    /**
     * Method to get an employee out of the employee response dto
     * @param dto the employee dto
     * @return the employee
     */
    public static Employee EmployeeResponseDTOToEmployee(EmployeeResponseDTO dto) {
        var employee = new Employee();
        employee.setId(dto.getId());
        employee.setLastName(dto.getLastName());
        employee.setFirstName(dto.getFirstName());
        employee.setStreet(dto.getStreet());
        employee.setPostcode(dto.getPostcode());
        employee.setCity(dto.getCity());
        employee.setPhone(dto.getPhone());
        employee.setSkillSet(dto.getSkillSet());

        return employee;
    }

    /**
     * Forms a dto out of an employee
     * @param employee the employee
     * @return EmployeeNameAndSkillDataDTO
     */
    public static EmployeeNameAndSkillDataDTO EmployeeEntityToNameAndSkillDataDTO(Employee employee){
        if (employee == null) return null;

        var dto = new EmployeeNameAndSkillDataDTO();
        dto.setId(employee.getId());
        dto.setLastName(employee.getLastName());
        dto.setFirstName(employee.getFirstName());

        dto.setSkillSet(employee.getSkillSet()
                .stream()
                .map(s -> new QualificationDto(s))
                .collect(Collectors.toSet()));

        return dto;
    }

    /**
     * Forms Employees out of employeesForAQualificationDTO
     * @param dto the employeesForAQualificationDTO
     * @return the employees
     */
    public static Set<Employee> employeesForAQualificationDTOToEmployee(EmployeesForAQualificationDTO dto){
        Set<Employee> employees = new HashSet<>();

        for (var dtoEmployee : dto.getEmployees()) {
            var employee = new Employee();
            employee.setId(dtoEmployee.getId());
            employee.setFirstName(dtoEmployee.getFirstName());
            employee.setLastName(dtoEmployee.getLastName());

            employees.add(employee);
        }

        return employees;
    }
}
