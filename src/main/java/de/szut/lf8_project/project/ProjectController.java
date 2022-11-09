package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.employee.EmployeeService;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
    @GetMapping("/{id}")
    public GetProjectDto readById(@PathVariable Long id){
        ProjectEntity entity = this.service.readById(id);
        return projectMapper.MapProjectToGetProjectDto(entity);
    }

    /**
     * Get Endpunkt für alle Projekte eines Mitarbeiters
     * @param employeeId
     * @return
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<GetProjectDto>> readByEmployeeId(@PathVariable Long employeeId) {
        List<GetProjectDto> response = this.service
                .readByEmployeeId(employeeId)
                .stream()
                .map(projectMapper::MapProjectToGetProjectDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }


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
            throw new ResourceNotFoundException("HelloEntity not found on id = " + id);
        } else {
            this.service.delete(entity);
        }
    }
}
