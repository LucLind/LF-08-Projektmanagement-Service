package de.szut.lf8_project.employee;

import de.szut.lf8_project.employee.dto.EmployeesForAQualificationDto;
import de.szut.lf8_project.employee.dto.GetEmployeeDto;
import de.szut.lf8_project.qualification.QualificationEntity;
import de.szut.lf8_project.qualification.QualificationMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class EmployeeMapper {

    public static GetEmployeeDto EntityToGetEmployeeDto(EmployeeEntity entity){
        var dto = new GetEmployeeDto();
        dto.setId(entity.getId());
        dto.setCity(entity.getCity());
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        dto.setStreet(entity.getStreet());
        dto.setPostcode(entity.getPostcode());
        dto.setPhone(entity.getPhone());
        return dto;
    }
    public static EmployeeEntity GetEmployeeDtoToEmployeeEntity(GetEmployeeDto dto){
        var entity = new EmployeeEntity();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setPostcode(dto.getPostcode());
        entity.setPhone(dto.getPhone());
        return entity;
    }

    public EmployeesForAQualificationDto mapToGetDto(EmployeeEntity entity) {
        return new EmployeesForAQualificationDto(entity.getId(), entity.getSkillSet());
    }
}
