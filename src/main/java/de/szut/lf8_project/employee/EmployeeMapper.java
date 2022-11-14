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

@Service
public class EmployeeMapper {

    public static EmployeeEntity GetEmployeeDtoToEmployeeEntity(EmployeeResponseDTO dto){
        var entity = new EmployeeEntity();
        entity.setId(dto.getId());

        return entity;
    }

    public static Employee EmployeeResponseDTOToEmployeeManagementEntity(EmployeeResponseDTO dto) {
        var entity = new Employee();
        entity.setId(dto.getId());
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setStreet(dto.getStreet());
        entity.setPostcode(dto.getPostcode());
        entity.setCity(dto.getCity());
        entity.setPhone(dto.getPhone());
        entity.setSkillSet(dto.getSkillSet());

        return entity;
    }

    public static EmployeeNameAndSkillDataDTO EmployeeEntityToNameAndSkillDataDTO(Employee entity){
        if (entity == null) return null;

        var dto = new EmployeeNameAndSkillDataDTO();
        dto.setId(entity.getId());
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());

        dto.setSkillSet(entity.getSkillSet()
                .stream()
                .map(s -> new QualificationDto(s))
                .collect(Collectors.toSet()));

        return dto;
    }

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

    public EmployeesForAQualificationDTO mapToGetDto(EmployeeEntity entity) {
        //return new EmployeesForAQualificationDto(entity.getId(), entity.getSkillSet());
        return null;
    }
    public EmployeeRequestDTO mapToGetEmployeeDto(EmployeeEntity entity) {
        return new EmployeeRequestDTO();
    }


}
