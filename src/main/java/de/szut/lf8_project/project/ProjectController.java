package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.Employee;
import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.employee.EmployeeService;
import de.szut.lf8_project.employee.dto.internal.EmployeeRoleDTO;
import de.szut.lf8_project.exceptionHandling.EmployeeNotFreeException;
import de.szut.lf8_project.exceptionHandling.EmployeeWrongSkillException;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import de.szut.lf8_project.project.dto.GetProjectEmployeesDTO;
import de.szut.lf8_project.project.dto.PutProjectDTO;
import de.szut.lf8_project.role.EmployeeRoleKey;
import de.szut.lf8_project.role.ProjectEmployeeRoleEntity;
import de.szut.lf8_project.role.ProjectEmployeeRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * the project controller class
 */
@RequestMapping("project")
@RestController
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper projectMapper;
    private final EmployeeService employeeService;
    private final ProjectEmployeeRoleService roleService;

    /**
     * the constructor
     */
    public ProjectController(ProjectService service, ProjectMapper projectMapper, EmployeeService employeeService, ProjectEmployeeRoleService roleService) {
        this.service = service;
        this.projectMapper = projectMapper;
        this.employeeService = employeeService;
        this.roleService = roleService;
    }

    //region |========================= Post Project =========================|

    /**
     * post endpoint fpr projects
     * @param dto the AddProjectDto
     * @param token the authorization token
     * @return response entity CREATED if the project was created
     */
    @Operation(summary = "erstellt ein neues Projekt mit Mitarbeiter/in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projekt erstellt",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "400", description = "ungültiges JSON gepostet",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<GetProjectDto> postProject(@RequestBody @Valid AddProjectDto dto,
                                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String token){

        Employee mainEmployee = null;
        Set<Employee> employees = null;

        if (dto.getMainEmployee() != null){
            mainEmployee = employeeService.readById(dto.getMainEmployee().getEmployeeId(), token);
            if (mainEmployee == null){
                throw new ResourceNotFoundException("No Employee found with ID: " + dto.getMainEmployee().getEmployeeId());
            }
            if (dto.getMainEmployee().getRole() != null &&
                    !mainEmployee.getSkillSet().contains(dto.getMainEmployee().getRole())){
                throw new EmployeeWrongSkillException("Employee with ID: " + mainEmployee.getId() + " lacks requested skill: " + dto.getMainEmployee().getRole());
            }
            if(!employeeService.employeeIsFree(mainEmployee, dto)){
                throw new EmployeeNotFreeException("Main employee is has no time for the requested project");
            }
        }
        if (!dto.getEmployees().isEmpty()){
            var employeeIds = dto.getEmployees().stream().map(EmployeeRoleDTO::getEmployeeId).collect(Collectors.toSet());
            employees = employeeService.readById(employeeIds, token);
            if (employees.isEmpty()){
                throw new EmployeeNotFreeException("No Employees were found.");
            }
        }

        // Projekt Entity erzeugt
        var project = projectMapper.MapAddProjectDtoToProject(dto, mainEmployee, employees);

        // Projekt Entity auf der Datenbank speichern.


        project = service.save(project);

        // Response erzeugt und zurückgegeben.
        var response = ProjectMapper.MapProjectToGetProjectDto(project);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    //endregion

    //region |========================= Get All =========================|

    /**
     * Get endpoint for all projects
     * @param token authorization token
     * @return response entity OK if all project were found
     */
    @Operation(summary = "gibt eine Liste von Projekten wieder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste aller Projekte",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<Set<GetProjectDto>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        var entities = service.readAll();

        var response = new HashSet<GetProjectDto>();

        for (ProjectEntity entity : entities) {
            response. add(ProjectMapper.MapProjectToGetProjectDto(entity));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //endregion

    // region |========================= Get by ID =========================|

    /**
     * get endpoint for a project
     *
     * @param id the project id
     * @param token the authorization header
     * @return respons entity (OK) if the project was foung
     */
    @Operation(summary = "finde Projekt anhand ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projekt gefunden",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "404", description = "Projekt anhand der ID nicht gefunden",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<GetProjectDto> getById(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        ProjectEntity entity = service.readById(id);
        if (entity == null){
            throw new ResourceNotFoundException("Project not found with Id: " + id);
        }

        var response = projectMapper.MapProjectToGetProjectDto(entity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //endregion

    //region |========================= Put Employee by Skill =========================|
    /**
     * Puts an Employee by Skill
     * @param projectId the project id
     * @param employeeId the employee ID
     * @param skill the qualifikation
     * @param token the authorization token
     * @return Response entity OK if the employee was put by skill (OK)
     */
    @Operation(summary = "fügt einem Projekt eine/n Mitarbeiter/in mit einer Rolle hinzu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mitarbeiter/in + Rolle hinzugefügt",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "404", description = "Projekt oder Mitarbeiter/in nicht gefunden",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung",
                    content = @Content)})
    @PutMapping("/{projectId}/employee/{employeeId}/qualification/{skill}")
    public ResponseEntity<GetProjectDto> addEmployeeBySkill(@PathVariable Long projectId,
                                                            @PathVariable Long employeeId,
                                                            @PathVariable String skill,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        ProjectEntity project = service.readById(projectId);
        Employee employee = employeeService.readById(employeeId, token);

        if (project == null && employee == null){
            throw new ResourceNotFoundException("No project with ID: " + projectId +  "/n/rNo employee with ID: " + employeeId);
        }
        else if( project == null){
            throw new ResourceNotFoundException("No project with ID: " + projectId);
        }
        else if( employee == null){
            throw new ResourceNotFoundException("No employee with ID: " + employeeId);
        }

        if (!employee.getSkillSet().contains(skill)){
            throw new EmployeeWrongSkillException("The requested employee lacks the desired skill: " + skill);
        }

        if (!employeeService.employeeIsFree(employee, project)){
            throw new EmployeeNotFreeException("Employee has no time for the requested project");
        }

        if (employeeService.employeeIsFree(employee, project)){
            var role = new ProjectEmployeeRoleEntity();
            role.setId(new EmployeeRoleKey(projectId, employeeId));
            role.setProject(project);
            role.setEmployee(employee.getEntity());
            role.setSkill(skill);
            roleService.save(role);
        }

        var dto = ProjectMapper.MapProjectToGetProjectDto(project);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    //endregion

    //region |========================= Delete Project =========================|
    /**
     * Deletes a Project by its id
     * @param id the project id
     */
    @Operation(summary = "Löscht ein Projekt anhand seiner ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Erfolgreich gelöscht"),
            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden",
                    content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProjectById(@PathVariable long id) {
        var entity = this.service.readById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Project not found with Id: " + id);
        }

        this.service.delete(entity);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    //endregion

    //region |========================= Remove Employee =========================|
    /**
     * deletes an employee out of a project and the project out of the employee
     * @param projectId the project ID
     * @param employeeId the employee Id
     */
    @Operation(summary = "Löscht eine/n Mitarbeiter/in anhand der ID aus einem Projekt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Erfolgreich gelöscht"),
            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Projekt oder Mitarbeiter nicht gefunden",
                    content = @Content)})
    @DeleteMapping("/{projectId}/employee/{employeeId}")
    public ResponseEntity deleteEmployeeFromProjectById(@PathVariable Long projectId, @PathVariable Long employeeId) {
        var entity = this.service.readById(projectId);
        if (entity == null) {
            throw new ResourceNotFoundException("Project not found with Id: " + projectId);
        }

        //Verantwortlichen Mitarbeiter finden und entfernen
        EmployeeEntity mainEmployee = entity.getMainEmployee();
        if (mainEmployee != null && mainEmployee.getId() == employeeId){
            entity.setMainEmployee(null);
            service.save(entity);

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        //Mitarbeiter aus involvierten Liste suchen und entfernen
        Set<ProjectEmployeeRoleEntity> employees = entity.getInvolvedEmployees();
        ProjectEmployeeRoleEntity EmployeetoBeFound = null;
        for (var role: employees) {
            if (Objects.equals(role.getEmployee().getId(), employeeId)){
                EmployeetoBeFound = role;
                break;
            }
        }
        if (EmployeetoBeFound == null){
            throw new ResourceNotFoundException("Employee with Id: " + employeeId + " , not found in Project.");
        }
        employees.remove(EmployeetoBeFound);
        entity.setInvolvedEmployees(employees);
        roleService.delete(EmployeetoBeFound);
        entity = service.save(entity);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    //endregion

    //region |===================== Get Employees from Project =========================|
    /**
     * Gets the employees of a project
     * @param id the project id
     * @param token the authorization token
     * @return return response entity OK if the project was found and the employees were found
     */
    @Operation(summary = "Gibt alle Mitarbeiter/innen eines Projekts zurück")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich geladen",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectEmployeesDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden",
                    content = @Content)})
    @GetMapping("{id}/employee")
    public ResponseEntity<GetProjectEmployeesDTO> getEmployeesFromProject(@PathVariable Long id,
                                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        ProjectEntity entity = service.readById(id);
        if (entity == null){
            throw new ResourceNotFoundException("Project not found with Id: " + id);
        }

        var mainEmployee = getMainEmployee(entity, token);
        var employees = getEmployees(entity, token);

        var response = ProjectMapper.mapProjectToGetProjectEmployeesDTO(entity, mainEmployee, employees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //endregion

    //region |========================= Put Project =========================|

    /**
     * Updates a project
     * @param id the project id
     * @param dto the PutProjectDto
     * @param token the authorization token
     * @return Response Entity OK if the project was found and is updated
     */
    @Operation(summary = "Update eines Projekts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projekt erfolgreich geupdated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<GetProjectDto> updateProject(@PathVariable Long id,
                                                       @RequestBody PutProjectDTO dto,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        var oldEntity = service.readById(id);
        if (oldEntity == null){
            throw new ResourceNotFoundException("No project found with ID: " + id);
        }
        var entity = this.projectMapper.mapAddProjectDtoToExistingProject(dto, oldEntity);
        entity = this.service.save(entity);
        var response = ProjectMapper.MapProjectToGetProjectDto(entity);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    //endregion

    //region |========================= Put Main Employee by Skill =========================|

    /**
     * Update the main amployee by his or her skill
     * @param projectId the project id
     * @param employeeId the employee id
     * @param skill the qualification
     * @param token the authorization token
     * @return response entity OK if the main employee is updated
     */
    @Operation(summary = "Einsetzen des Hauptverantwortlichen anhand seiner Qualifikation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hauptverantwortlicher erfolgreich geupdated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Hauptverantwortlicher nicht gefunden",
                    content = @Content)})
    @PutMapping("/{projectId}/mainEmployee/{employeeId}/qualification/{skill}")
    public ResponseEntity<GetProjectDto> addMainEmployeeBySkill(@PathVariable Long projectId,
                                                                @PathVariable Long employeeId,
                                                                @PathVariable String skill,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        ProjectEntity project = service.readById(projectId);
        Employee employee = employeeService.readById(employeeId, token);

        if (project == null && employee == null){
            throw new ResourceNotFoundException("No project with ID: " + projectId +  "/n/rNo employee with ID: " + employeeId);
        }
        else if( project == null){
            throw new ResourceNotFoundException("No project with ID: " + projectId);
        }
        else if( employee == null){
            throw new ResourceNotFoundException("No employee with ID: " + employeeId);
        }

        if (!employee.getSkillSet().contains(skill)){
            throw new EmployeeWrongSkillException("The requested employee lacks the desired skill: " + skill);
        }

        if (!employeeService.employeeIsFree(employee, project)){
            throw new EmployeeNotFreeException("Employee is has no time for the requested project");
        }

        if (employeeService.employeeIsFree(employee, project)){
            project.setMainEmployee(employee.getEntity());
            project.setMainEmployeeQualification(skill);
        }

        var dto = ProjectMapper.MapProjectToGetProjectDto(project);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    //endregion
    /**
     * Helper method for getting an employee object out of the employee entity.
     * @param project the Project
     * @param token the authorization token
     * @return the employee object
     */
    private Employee getMainEmployee(ProjectEntity project, String token){
        if (project.getMainEmployee() == null){
            return null;
        }

        return employeeService.readById(project.getMainEmployee().getId(), token);
    }

    /**
     * Helper method for getting an employee objects out of the involved employee entities.
     * @param project the project
     * @param token the authorization token
     * @return the employee objects
     */
    private Set<Employee> getEmployees(ProjectEntity project, String token){
        if (project.getInvolvedEmployees() == null ||
        project.getInvolvedEmployees().isEmpty()){
            return new HashSet<>();
        }

        var ids = project.getInvolvedEmployees().stream().map(e -> e.getEmployee().getId()).collect(Collectors.toSet());
        return employeeService.readById(ids, token);
    }
}
