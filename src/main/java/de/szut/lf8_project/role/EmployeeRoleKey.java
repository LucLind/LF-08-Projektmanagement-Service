package de.szut.lf8_project.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * employee role key class
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmployeeRoleKey implements Serializable {

    @Column(name = "project_Id", nullable = false)
    Long projectId;

    @Column(name = "employee_Id", nullable = false)
    Long employeeId;
}
