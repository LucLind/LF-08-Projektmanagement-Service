package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PutProjectIT extends AbstractIntegrationTest {
    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/project/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void putEmployee() throws Exception {
        var project1 = new ProjectEntity();
        project1.setDescription("Eine Beschreibung");
        project1.setMainEmployee(null);
        project1.setInvolvedEmployees(null);
        project1.setCustomer(null);
        project1.setComment("Ein Kommentar");
        project1.setStartDate(new Date(2022, 1, 1));
        project1.setEstimatedEndDate(new Date(2023, 1, 1));
        projectRepository.save(project1);

        final String content = """
                {
                    "description": "Eine neue Beschreibung",
                    "comment": "Wir wissen noch nichts",
                    "startDate": "2040-01-01",
                    "estimatedEndDate": "2041-01-01",
                    "customerId": 3
                }
                """;

        final var contentAsString = this.mockMvc.perform(put("/project/1")
                        .header("Authorization", bearerToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("description", is("Eine neue Beschreibung")));

    }
}
