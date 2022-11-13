package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.Employee;
import de.szut.lf8_project.employee.EmployeeService;
import de.szut.lf8_project.exceptionHandling.EmployeeNotFreeException;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("project")
@RestController
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper projectMapper;
    private final EmployeeService employeeService;

    public ProjectController(ProjectService service, ProjectMapper projectMapper, EmployeeService employeeService) {
        this.service = service;
        this.projectMapper = projectMapper;
        this.employeeService = employeeService;
    }

    //region Create
    /**
     * Post Endpunkt für Projekte anlegen
     * @param dto
     * @return
     */
    @Operation(summary = "erstellt ein neues Projekt mit Mitarbeiter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projekt erstellt",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "400", description = "ungültiges JSON gepostet",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<GetProjectDto> createProject(@RequestBody @Valid AddProjectDto dto,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token){

        // Existierende Mitarbeiter aus dem Mitarbeiter Service laden.
        Employee mainEmployee = null;
        Set<Employee> employees = null;

        if (dto.getMainEmployeeId() != null){
            mainEmployee = employeeService.readById(dto.getMainEmployeeId(), token);
            if (mainEmployee == null){
                return null;
            }
            if(!employeeService.employeeIsFree(mainEmployee, dto)){
                throw new EmployeeNotFreeException("Main employee is has no time for the requested project");
            }
        }
        if (!dto.getEmployees().isEmpty()){
            employees = employeeService.readById(dto.getEmployees(), token);
            if (employees.isEmpty()){
                return null;
            }
        }

        // Projekt Entity erzeugt
        var project = projectMapper.MapAddProjectDtoToProject(dto, mainEmployee, employees);

        // Projekt Entity auf der Datenbank speichern.


        service.save(project);

        // Response erzeugt und zurückgegeben.
        var response = projectMapper.MapProjectToGetProjectDto(project, mainEmployee, employees);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    //endregion

    //region Find All
    /**
     * Get Endpunkt für alle Projekte
     * @return
     */
    @Operation(summary = "gibt eine Liste von Projekten wieder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste aller Projekte",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<Set<GetProjectDto>> findAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        var entities = service.readAll();

        var response = new HashSet<GetProjectDto>();

        for (ProjectEntity entity : entities) {
            var mainEmployee = getMainEmployee(entity, token);
            var employees = getEmployees(entity, token);

            response. add(projectMapper.MapProjectToGetProjectDto(entity, mainEmployee, employees));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //endregion

    // region Read By Id
    /**
     * Get Endpunkt für ein Projekt
     * @param id
     * @return
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
    public ResponseEntity<GetProjectDto> readById(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        ProjectEntity entity = service.readById(id);
        if (entity == null){
            throw new ResourceNotFoundException("Project not found with Id: " + id);
        }

        var mainEmployee = getMainEmployee(entity, token);
        var employees = getEmployees(entity, token);

        var response = projectMapper.MapProjectToGetProjectDto(entity, mainEmployee, employees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //endregion

    @PutMapping("/{id}/qualification/{skill}")
    public ResponseEntity<GetProjectDto> addEmployeeBySkill(@PathVariable Long id,
                                                            @PathVariable String skill,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        ProjectEntity project = service.readById(id);
        Set<Employee> employees = employeeService.readBySkill(skill, token);

        for (Employee e : employees) {
            if (employeeService.employeeIsFree(e, project)){
                project.setMainEmployee(e.getEntity());
                project = service.save(project);
                break;
            }
        }

        Employee mainEmployee = getMainEmployee(project, token);
        employees = getEmployees(project, token);
        var dto = projectMapper.MapProjectToGetProjectDto(project, mainEmployee, employees);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    /**
     * Get Endpunkt für alle Projekte eines Mitarbeiters
     * @param employeeId
     * @return
     */
    @Operation(summary = "finde alle Projekte eines Mitarbeiters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projekte des Mitarbeiters gefunden",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDto.class))}),
            @ApiResponse(responseCode = "404", description = "Projekte anhand der Mitarbeiter-ID nicht gefunden",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung",
                    content = @Content)})
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<GetProjectDto>> readByEmployeeId(@PathVariable Long employeeId,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//        var employee = employeeService.readById(employeeId, token);
//        var projects = service.readByEmployeeId(employeeId);
//
//        List<GetProjectDto> response = this.service
//                .readByEmployeeId(employeeId)
//                .stream()
//                .map(p -> projectMapper.MapProjectToGetProjectDto(p, employee))
//                .collect(Collectors.toList());

        return null;
    }

    /**
     * Delete Endpunkt für ein Projekt
     * @param id
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
            throw new ResourceNotFoundException("Projekt mit nachfolgender ID nicht gefunden = " + id);
        }

        this.service.delete(entity);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Löscht einen Mitarbeiter aus einem Projekt und das Projekt aus dem MA
     * @param id die Projekt-Id
     * @param employeeId die MA-Id
     */
//    @Operation(summary = "Löscht einen Mitarbeiter anhand seiner ID aus einem Projekt")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Erfolgreich gelöscht"),
//            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
//                    content = @Content),
//            @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden",
//                    content = @Content)})
//    @DeleteMapping("/{employeeId}")
//    @ResponseStatus(code = HttpStatus.NO_CONTENT)
//    public void deleteEmployeeFromProjectById(@PathVariable long id, long employeeId) {
//        var entity = this.service.readById(id);
//        if (entity == null) {
//            throw new ResourceNotFoundException("Projekt mit nachfolgender ID nicht gefunden = " + id);
//        } else {
//            //MA aus Projekt entfernen
//            Set<EmployeeEntity> employees = entity.getInvolvedEmployees();
//            EmployeeEntity EmployeetoBeFound = null;
//            for (EmployeeEntity ent: employees) {
//                if (ent.getId() == employeeId){
//                    EmployeetoBeFound = ent;
//                    break;
//                }
//            }
//            if (EmployeetoBeFound == null){
//                throw new ResourceNotFoundException("Mitarbeiter mit nachfolgender ID nicht gefunden = " + employeeId);
//            }
//            employees.remove(EmployeetoBeFound);
//
//            //Projekt aus MA entfernen (involved)
//            Set<ProjectEntity> involvedProjects = EmployeetoBeFound.getInvolvedProjects();
//            ProjectEntity invProject = null;
//            for (ProjectEntity ent: involvedProjects) {
//                if (ent.getId() == id){
//                    invProject = ent;
//                    break;
//                }
//            }
//            involvedProjects.remove(invProject);
//        }
//    }


    /**
     * Update Endpunkt für ein Projekt
     * @param id
     * @param dto
     */
//    @Operation(summary = "Update eines Projekts")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Projekt erfolgreich geupdated",
//                    content = {@Content(mediaType = "application/json",
//                            schema = @Schema(implementation = GetProjectDto.class))}),
//            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
//                    content = @Content),
//            @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden",
//                    content = @Content)})
//    @PutMapping("/{id}")
//    public ResponseEntity<GetProjectDto> updateProject(@PathVariable Long id,
//                                                       @RequestBody AddProjectDto dto,
//                                                       EmployeeMangementEntity mainEmployee,
//                                                       Set<EmployeeMangementEntity> employees) {
//        var entity = this.projectMapper.MapAddProjectDtoToProject(dto, mainEmployee, employees);
//        entity.setId(id);
//        entity = this.service.update(entity);
//        return null;//ResponseEntity.ok(this.projectMapper.MapProjectToGetProjectDto(entity));
//    }


        private Employee getMainEmployee(ProjectEntity project, String token){
            if (project.getMainEmployee() == null){
                return null;
            }

            return employeeService.readById(project.getMainEmployee().getId(), token);
        }

        private Set<Employee> getEmployees(ProjectEntity project, String token){
            if (project.getInvolvedEmployees() == null ||
            project.getInvolvedEmployees().isEmpty()){
                return new HashSet<>();
            }

            var ids = project.getInvolvedEmployees().stream().map(e -> e.getId()).collect(Collectors.toSet());
            return employeeService.readById(ids, token);
        }
    }
