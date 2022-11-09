package de.szut.lf8_project.employee;

import de.szut.lf8_project.employee.dto.EmployeesForAQualificationDto;
import de.szut.lf8_project.employee.dto.GetEmployeeDto;
import de.szut.lf8_project.project.dto.GetProjectDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("employee")
@RestController
public class EmployeeController {
    private final EmployeeService service;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService service, EmployeeMapper employeeMapper){
        this.service = service;
        this.employeeMapper = employeeMapper;
    }


    /**
     * Get Endpunkt: findet Mitarbeiter anhand ihrer Qualifikation
     * @params employeeId skillSet
     * @return Eine Liste an Mitarbeitern anhand der gewählten Qualifikation
     */
    @Operation(summary = "Findet Mitarbeiter anhand Qualifikation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste der Mitarbeiter mit jeweiliger Qualifikation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeesForAQualificationDto.class))}),
            @ApiResponse(responseCode = "404", description = "Qualifikation nicht gefunden",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
                    content = @Content)})
    @GetMapping("/qualification")
    public ResponseEntity<List<EmployeesForAQualificationDto>> findAllEmployeesByQualification(@RequestParam String skill) {
        List<EmployeesForAQualificationDto> response = this.service
                .findBySkill(skill)
                .stream()
                .map(e -> this.employeeMapper.mapToGetDto(e))
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    /**
     * Get Endpunkt: findet Mitarbeiter anhand Projekt-ID
     * @params id
     * @return Eine Liste an Mitarbeitern anhand der gewählten Projekt-ID
     */
    @Operation(summary = "Findet Mitarbeiter anhand Projekt-ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste der Mitarbeiter mit jeweiliger Projekt-ID",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEmployeeDto.class))}),
            @ApiResponse(responseCode = "404", description = "Projekt-ID nicht gefunden",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<List<GetEmployeeDto>> findAllEmployeesByProjectId(@PathVariable Long id) {
        List<GetEmployeeDto> response = this.service
                .readByProjectId(id)
                .stream()
                .map(e -> this.employeeMapper.mapToGetEmployeeDto(e))
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }
}
