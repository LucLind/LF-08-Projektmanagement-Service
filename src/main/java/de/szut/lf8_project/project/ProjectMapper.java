package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.Employee;
import de.szut.lf8_project.employee.EmployeeMapper;
import de.szut.lf8_project.employee.dto.internal.EmployeeNameRoleDTO;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import de.szut.lf8_project.project.dto.GetProjectEmployeesDTO;
import de.szut.lf8_project.role.EmployeeRoleKey;
import de.szut.lf8_project.role.ProjectEmployeeRoleEntity;
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
                entity.setInvolvedEmployees(
                        employees.stream().map(e -> {
                            var r = new ProjectEmployeeRoleEntity();
                            r.setEmployee(e.getEntity());
                            r.setProject(entity);
                            r.setSkill(e.getSkillSet().stream().findFirst().orElse(""));
                            r.setId(new EmployeeRoleKey());
                            return r;
                        }).collect(Collectors.toSet()));
            }

            // gefüllte entity zurückgeben
            return entity;
        }

        public static GetProjectDto MapProjectToGetProjectDto(ProjectEntity project){
            GetProjectDto dto = new GetProjectDto();
            dto.setId(project.getId());
            dto.setDescription(project.getDescription());
            dto.setComment(project.getComment());
            dto.setStartDate(project.getStartDate());
            dto.setEstimatedEndDate(project.getEstimatedEndDate());
            dto.setFinalEndDate(project.getFinalEndDate());

            if (project.getMainEmployee() != null) {
                dto.setMainEmployee(project.getMainEmployee().getId());
            }

            if (project.getInvolvedEmployees() != null) {
                var employeeDtos = project.getInvolvedEmployees()
                        .stream()
                        .map(e -> e.getEmployee().getId())
                        .collect(Collectors.toSet());
                dto.setEmployees(employeeDtos);
            }
            else{
                dto.setEmployees(new HashSet<>());
            }

            dto.setCustomer(357l);
            return dto;
        }

        public static GetProjectEmployeesDTO mapProjectToGetProjectEmployeesDTO(ProjectEntity project, Employee mainEmployee, Set<Employee> employees){
            var dto = new GetProjectEmployeesDTO();

            dto.setProjectId(project.getId());
            dto.setDescription(project.getDescription());

            if (mainEmployee != null) {
                var mainEmployeeDto = new EmployeeNameRoleDTO();
                mainEmployeeDto.setEmployeeId(mainEmployee.getId());
                mainEmployeeDto.setLastName(mainEmployeeDto.getLastName());
                mainEmployeeDto.setRole(project.getMainEmployeeQualification());
                dto.setMainEmployee(mainEmployeeDto);
            }

            var employeeDtos = new HashSet<EmployeeNameRoleDTO>();
            for (Employee empl : employees){
                var eDto = new EmployeeNameRoleDTO();
                eDto.setEmployeeId(empl.getId());
                eDto.setLastName(empl.getLastName());
                for (ProjectEmployeeRoleEntity p : empl.getEntity().getInvolvedProjects()){
                    if (p.getProject().getId().equals(project.getId())){
                        eDto.setRole(p.getSkill());
                        break;
                    }
                }
                employeeDtos.add(eDto);
            }
            dto.setEmployees(employeeDtos);

            return dto;
        }
}
