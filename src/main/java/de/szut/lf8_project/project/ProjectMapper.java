package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.Employee;
import de.szut.lf8_project.employee.EmployeeMapper;
import de.szut.lf8_project.employee.dto.internal.EmployeeNameRoleDTO;
import de.szut.lf8_project.employee.dto.internal.EmployeeRoleDTO;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import de.szut.lf8_project.project.dto.GetProjectEmployeesDTO;
import de.szut.lf8_project.project.dto.PutProjectDTO;
import de.szut.lf8_project.role.EmployeeRoleKey;
import de.szut.lf8_project.role.ProjectEmployeeRoleEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project mapper class
 */
@Service
public class ProjectMapper {


    /**
     * makes up new empty entity and fills it with data
     * @param dto the addProjectDto
     * @param mainEmployee the main employee
     * @param employees the set of employees
     * @return returns a project entity
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

        // gef??llte entity zur??ckgeben
        return entity;
    }

    /**
     * maps the put project dto to an existing project
     * @param dto the put project dto
     * @param entity the project entity
     * @return return the mapped project entity
     */
    public static ProjectEntity mapAddProjectDtoToExistingProject(PutProjectDTO dto, ProjectEntity entity){
        entity.setDescription(dto.getDescription());
        entity.setComment(dto.getComment());
        entity.setStartDate(dto.getStartDate());
        entity.setEstimatedEndDate(dto.getEstimatedEndDate());
        entity.setFinalEndDate(dto.getFinalEndDate());

        return entity;
    }

    /**
     * maps a project to GetProjectDto
     * @param project the project
     * @return the GetProjectDto
     */
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

    /**
     * maps Project to GetProjectEmployeesDTO
     * @param project the project
     * @param mainEmployee the main amployee
     * @param employees the set of employees
     * @return returns the GetProjectEmployeesDTO
     */
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
