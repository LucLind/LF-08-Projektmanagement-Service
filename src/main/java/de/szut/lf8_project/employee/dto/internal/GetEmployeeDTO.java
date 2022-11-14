package de.szut.lf8_project.employee.dto.internal;

import de.szut.lf8_project.employee.dto.external.EmployeeResponseDTO;
import lombok.Data;

import java.util.Set;

@Data
public class GetEmployeeDTO {
    private EmployeeResponseDTO employee;
    private Long mainProjectId;
    private Set<Long> involvedProjectIds;
}
