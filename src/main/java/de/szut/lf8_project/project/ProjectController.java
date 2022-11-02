package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.employee.EmployeeService;
import de.szut.lf8_project.project.dto.AddProjectDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

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


}
