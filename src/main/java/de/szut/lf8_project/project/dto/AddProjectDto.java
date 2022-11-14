package de.szut.lf8_project.project.dto;

import de.szut.lf8_project.employee.EmployeeEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
public class AddProjectDto {
    @NotBlank(message = "Description is mandatory")
    private String description;

    private Long mainEmployeeId;

    private Set<Long> employees;

    @NotBlank(message = "Comment is mandatory")
    private String comment;

    @NotNull(message = "Start date is mandatory")
    private Date startDate;

    @NotNull(message = "Estimated end date is mandatory")
    private Date estimatedEndDate;

    @NotNull(message = "Customer is mandatory")
    private Long customerId;

    private Date finalEndDate;
}
