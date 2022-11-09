package de.szut.lf8_project.employee;

import de.szut.lf8_project.employee.dto.EmployeesForAQualificationDto;
import de.szut.lf8_project.employee.dto.GetEmployeeDto;
import de.szut.lf8_project.qualification.QualificationEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EmployeeMapper {

    public EmployeeEntity GetEmployeeDtoToEmployeeEntity(GetEmployeeDto dto, Set<QualificationEntity> skillSet){
        var entity = new EmployeeEntity();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setPostcode(dto.getPostcode());
        entity.setPhone(dto.getPhone());
        entity.setSkillSet(skillSet);

        return entity;
    }

    public EmployeesForAQualificationDto mapToGetDto(EmployeeEntity entity) {
        return new EmployeesForAQualificationDto(entity.getId(), entity.getSkillSet());
    }
}
