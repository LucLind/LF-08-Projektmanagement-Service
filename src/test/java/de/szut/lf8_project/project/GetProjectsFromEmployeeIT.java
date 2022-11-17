package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for getting projects from an employee
 */
public class GetProjectsFromEmployeeIT extends AbstractIntegrationTest {
    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/employee/"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * puts an employee
     * @throws Exception exception
     */
    @Test
    @WithMockUser(roles = "user")
    void putEmployee() throws Exception {

        var employee1 = new EmployeeEntity(12L, null, null);
        employeeRepository.save(employee1);

        var project1 = new ProjectEntity();
        project1.setDescription("Eine Beschreibung");
        project1.setMainEmployee(employee1);
        project1.setInvolvedEmployees(null);
        project1.setCustomer(null);
        project1.setComment("Ein Kommentar");
        project1.setStartDate(new Date(2022, 1, 1));
        project1.setEstimatedEndDate(new Date(2023, 1, 1));

        var project2 = new ProjectEntity();
        project2.setDescription("Eine zweite Beschreibung");
        project2.setMainEmployee(employee1);
        project2.setInvolvedEmployees(null);
        project2.setCustomer(null);
        project2.setComment("Ein zweiter Kommentar");
        project2.setStartDate(new Date(2032, 1, 1));
        project2.setEstimatedEndDate(new Date(2033, 1, 1));

        projectRepository.save(project1);
        projectRepository.save(project2);

        final var contentAsString = this.mockMvc.perform(get("/employee/12/project")
                        .header("Authorization", bearerToken))
                .andExpect(status().is2xxSuccessful());
    }
}
