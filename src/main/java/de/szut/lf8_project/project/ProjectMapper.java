package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import org.springframework.stereotype.Service;

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
        public ProjectEntity MapAddProjectDtoToProject(AddProjectDto dto, EmployeeEntity mainEmployee, Set<EmployeeEntity> employees){
            // Leere Entity erzeugen
            ProjectEntity entity = new ProjectEntity();

            // Daten aus dto zuweisen.
            entity.setDescription(dto.getDescription());
            entity.setComment(dto.getComment());
            entity.setStartDate(dto.getStartDate());
            entity.setEstimatedEndDate(dto.getEstimatedEndDate());
            entity.setFinalEndDate(dto.getFinalEndDate());

            // Mitarbeiter zuweisen
//            entity.setMainEmployee(mainEmployee);
//            entity.setEmployees(employees);

            // gefüllte entity zurückgeben
            return entity;
        }
        public GetProjectDto MapProjectToGetProjectDto(ProjectEntity entity){
            GetProjectDto dto = new GetProjectDto();
            dto.setId(entity.getId());
            dto.setDescription(entity.getDescription());
            dto.setComment(entity.getComment());
            dto.setStartDate(entity.getStartDate());
            dto.setEstimatedEndDate(entity.getEstimatedEndDate());
            dto.setFinalEndDate(entity.getFinalEndDate());
//            dto.setMainEmployee(entity.getMainEmployee().getId());
//            dto.setEmployees(
//                    entity
//                            .getEmployees()
//                            .stream()
//                            .map(EmployeeEntity::getId)
//                            .collect(Collectors.toSet()));
            return dto;
        }
}
