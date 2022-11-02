package de.szut.lf8_project.project.dto;

import de.szut.lf8_project.employee.EmployeeEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Data
public class AddProjectDto {
    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Main employee is mandatory")
    private Long mainEmployeeId;

    @NotBlank(message = "Employees are mandatory")
    private Set<Long> employees;

    @NotBlank(message = "Comment is mandatory")
    private String comment;

    @NotBlank(message = "Start date is mandatory")
    private Date startDate;

    @NotBlank(message = "Estimated end date is mandatory")
    private Date estimatedEndDate;

    @NotBlank(message = "Customer is mandatory")
    private Long customerId;

    private Date finalEndDate;
}
