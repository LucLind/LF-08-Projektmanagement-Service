package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.employee.EmployeeService;
import de.szut.lf8_project.employee.dto.GetEmployeeDto;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<GetProjectDto> createProject(@RequestBody @Valid AddProjectDto dto){
        // Existierende Mitarbeiter aus dem Mitarbeiter Service laden.
        EmployeeEntity mainEmployee = employeeService.readById(dto.getMainEmployeeId());
        Set<EmployeeEntity> employees = employeeService.readById(dto.getEmployees());

        // Not Found Response zurückgeben wenn, Mitarbeiter nicht gefunden wurden.
        // TODO: Was ist wenn nicht alle Mitarbeiter in der Liste gefunden wurden?
        if (mainEmployee == null || employees.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Projekt Entity erzeugt
        ProjectEntity entity = projectMapper.MapAddProjectDtoToProject(dto, mainEmployee, employees);

        // Projekt Entity auf der Datenbank speichern.
        entity = service.create(entity);

        // Response erzeugt und zurückgegeben.
        GetProjectDto response = projectMapper.MapProjectToGetProjectDto(entity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
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
    public List<GetProjectDto> findAll(){
        return this.service
                .readAll()      // get all project
                .stream()   // convert to stream
                .map(projectMapper::MapProjectToGetProjectDto)   // map each project to a GetProjectDTO
                .collect(Collectors.toList());   // collect the results in a list
    }
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
    public ResponseEntity<GetProjectDto> readById(@PathVariable Long id){
        ProjectEntity entity = this.service.readById(id);
        return ResponseEntity.ok(projectMapper.MapProjectToGetProjectDto(entity));
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
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<GetProjectDto>> readByEmployeeId(@PathVariable Long employeeId) {
        List<GetProjectDto> response = this.service
                .readByEmployeeId(employeeId)
                .stream()
                .map(projectMapper::MapProjectToGetProjectDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
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
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteProjectById(@PathVariable long id) {
        var entity = this.service.readById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Projekt mit nachfolgender ID nicht gefunden = " + id);
        } else {
            this.service.delete(entity);
        }
    }

    /**
     * Löscht einen Mitarbeiter aus einem Projekt und das Projekt aus dem MA
     * @param id die Projekt-Id
     * @param employeeId die MA-Id
     */
    @Operation(summary = "Löscht einen Mitarbeiter anhand seiner ID aus einem Projekt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Erfolgreich gelöscht"),
            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden",
                    content = @Content)})
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEmployeeFromProjectById(@PathVariable long id, long employeeId) {
        var entity = this.service.readById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Projekt mit nachfolgender ID nicht gefunden = " + id);
        } else {
            //MA aus Projekt entfernen
            Set<EmployeeEntity> employees = entity.getEmployees();
            EmployeeEntity EmployeetoBeFound = null;
            for (EmployeeEntity ent: employees) {
                if (ent.getId() == employeeId){
                    EmployeetoBeFound = ent;
                    break;
                }
            }
            if (EmployeetoBeFound == null){
                throw new ResourceNotFoundException("Mitarbeiter mit nachfolgender ID nicht gefunden = " + employeeId);
            }
            employees.remove(EmployeetoBeFound);

            //Projekt aus MA entfernen (involved)
            List<ProjectEntity> involvedProjects = EmployeetoBeFound.getInvolvedProjects();
            ProjectEntity invProject = null;
            for (ProjectEntity ent: involvedProjects) {
                if (ent.getId() == id){
                    invProject = ent;
                    break;
                }
            }
            involvedProjects.remove(invProject);
        }
    }


    /**
     * Update Endpunkt für ein Projekt
     * @param id
     * @param dto
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
    public ResponseEntity<GetProjectDto> updateProject(@PathVariable Long id, @RequestBody AddProjectDto dto, EmployeeEntity mainEmployee, Set<EmployeeEntity> employees) {
        ProjectEntity entity = this.projectMapper.MapAddProjectDtoToProject(dto, mainEmployee, employees);
        entity.setId(id);
        entity = this.service.update(entity);
        return ResponseEntity.ok(this.projectMapper.MapProjectToGetProjectDto(entity));
    }

    }
