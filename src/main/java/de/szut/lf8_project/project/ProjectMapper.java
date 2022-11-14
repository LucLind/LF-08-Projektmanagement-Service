package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.Employee;
import de.szut.lf8_project.employee.EmployeeMapper;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectMapper {


    /**
     * Erzeugt leere Entity und weist daten zu.
     * @param dto
     * @param mainEmployee
     * @param employees
     * @return
     */
        public ProjectEntity MapAddProjectDtoToProject(AddProjectDto dto, Employee mainEmployee, Set<Employee> employees){
            // Leere Entity erzeugen
            ProjectEntity entity = new ProjectEntity();

            // Daten aus dto zuweisen.
            entity.setDescription(dto.getDescription());
            entity.setComment(dto.getComment());
            entity.setStartDate(dto.getStartDate());
            entity.setEstimatedEndDate(dto.getEstimatedEndDate());
            entity.setFinalEndDate(dto.getFinalEndDate());

            // Mitarbeiter zuweisen
            if (mainEmployee != null) {
                entity.setMainEmployee(mainEmployee.getEntity());
            }
            if(employees != null) {
                entity.setInvolvedEmployees(employees.stream().map(e -> e.getEntity()).collect(Collectors.toSet()));
            }

            // gefüllte entity zurückgeben
            return entity;
        }

        public GetProjectDto MapProjectToGetProjectDto(ProjectEntity project, Employee mainEmployee, Set<Employee> employees){
            GetProjectDto dto = new GetProjectDto();
            dto.setId(project.getId());
            dto.setDescription(project.getDescription());
            dto.setComment(project.getComment());
            dto.setStartDate(project.getStartDate());
            dto.setEstimatedEndDate(project.getEstimatedEndDate());
            dto.setFinalEndDate(project.getFinalEndDate());

            var mainEmployeeDTO = EmployeeMapper.EmployeeEntityToNameAndSkillDataDTO(mainEmployee);
            dto.setMainEmployee(mainEmployeeDTO);

            if (employees != null) {
                var employeeDtos = employees
                        .stream()
                        .map(e -> EmployeeMapper.EmployeeEntityToNameAndSkillDataDTO(e))
                        .collect(Collectors.toSet());
                dto.setEmployees(employeeDtos);
            }
            else{
                dto.setEmployees(new HashSet<>());
            }

            //dto.setCustomer(entity.getCustomer().getId());
            dto.setCustomer(357l);
            return dto;
        }
}
