package de.szut.lf8_project.employee;

import de.szut.lf8_project.employee.dto.EmployeesForAQualificationDto;
import de.szut.lf8_project.employee.dto.GetEmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
     * Put Endpunkt: setzt Mitarbeiter anhand ihrer Qualifikation in ein Projekt ein
     * @params employeeId skillSet
     * @return Eine Liste an Mitarbeitern anhand der gewählten Qualifikation
     */
//    @Operation(summary = "Findet Mitarbeiter anhand Qualifikation")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Liste der Mitarbeiter mit jeweiliger Qualifikation",
//                    content = {@Content(mediaType = "application/json",
//                            schema = @Schema(implementation = EmployeesForAQualificationDto.class))}),
//            @ApiResponse(responseCode = "404", description = "Qualifikation nicht gefunden",
//                    content = @Content),
//            @ApiResponse(responseCode = "401", description = "Zugriff verweigert",
//                    content = @Content)})
//    @GetMapping("/qualifikation")
//    public List<EmployeesForAQualificationDto> findAllEmployeesByQualification(@RequestParam String skill) {
//        return this.service
//                .findBySkill(skill)
//                .stream()
//                .map(e -> this.employeeMapper.mapToGetDto(e))
//                .collect(Collectors.toList());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<GetEmployeeDto> findById(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        var entity = this.service.readById(id, token);
        var dto = EmployeeMapper.EntityToGetEmployeeDto(entity);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
