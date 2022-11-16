package de.szut.lf8_project.employee;

import de.szut.lf8_project.employee.dto.external.EmployeeNameAndSkillDataDTO;
import de.szut.lf8_project.employee.dto.external.EmployeeRequestDTO;
import de.szut.lf8_project.employee.dto.external.EmployeeResponseDTO;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.project.ProjectEntity;
import de.szut.lf8_project.project.ProjectMapper;
import de.szut.lf8_project.project.ProjectService;
import de.szut.lf8_project.project.dto.GetProjectDto;
import de.szut.lf8_project.qualification.dto.EmployeesForAQualificationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Employee Controller
 */
@RequestMapping("employee")
@RestController
public class EmployeeController {
    private final EmployeeService service;
    private final EmployeeMapper employeeMapper;
    private final ProjectService projectService;

    /**
     * constructor
     * @param service the employee service
     * @param projectService the project service
     * @param employeeMapper the employee mapper
     */
    public EmployeeController(EmployeeService service, ProjectService projectService, EmployeeMapper employeeMapper){
        this.service = service;
        this.projectService = projectService;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Method for getting projects of an employee
     * @param id employee id
     * @param token authorization token
     * @return response entity OK if the employee (and its projects) is(were) found
     */
    @Operation(summary = "Alle Projekte eine/s/r Mitarbeiter/s/in auslesen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projekte erfolgreich ausgelesen",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Projekte nicht gefunden",
                    content = @Content) })
    @GetMapping("{id}/project")
    public ResponseEntity<Set<GetProjectDto>> getEmployeeProjects(@PathVariable Long id,
                                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        var employee = service.readById(id, token);
        if (employee == null){
            throw new ResourceNotFoundException("Employee not found with Id: " + id);
        }

        var response = new HashSet<GetProjectDto>();

        if (employee.getEntity().getMainProject() != null && !employee.getEntity().getMainProject().isEmpty()){
            for (var project : employee.getEntity().getMainProject()){
                response.add(ProjectMapper.MapProjectToGetProjectDto(project));
            }
        }
        for (var role : employee.getEntity().getInvolvedProjects()){
            response.add(ProjectMapper.MapProjectToGetProjectDto(role.getProject()));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
