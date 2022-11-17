package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Get employees from project class
 */
public class GetEmployeesFromProjectIT extends AbstractIntegrationTest {
    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/project/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void putEmployee() throws Exception {

        var project1 = new ProjectEntity();
        var employee1 = new EmployeeEntity(12L, null, null);
        var employee2 = new EmployeeEntity(117L, null, null);
        var employee3 = new EmployeeEntity(23L, null, null);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        project1.setDescription("Eine Beschreibung");
        project1.setMainEmployee(null);
        project1.setInvolvedEmployees(null);
        project1.setCustomer(null);
        project1.setComment("Ein Kommentar");
        project1.setStartDate(new Date(2022, 1, 1));
        project1.setEstimatedEndDate(new Date(2023, 1, 1));

        employeeRepository.save(employee1);
        projectRepository.save(project1);

        this.mockMvc.perform(put("/project/1/employee/9/qualification/weise")
                .header("Authorization", bearerToken));
        this.mockMvc.perform(put("/project/1/employee/17/qualification/Java")
                .header("Authorization", bearerToken));

        final var contentAsString = this.mockMvc.perform(get("/project/1/employee")
                        .header("Authorization", bearerToken))
                .andExpect(status().is2xxSuccessful());
    }
}
