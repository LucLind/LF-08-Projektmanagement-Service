package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteProjectIT extends AbstractIntegrationTest {
    @Test
    void authorization() throws Exception {
        final String content = """
                {
                    "description": "Unser erstes Projekt",
                    "mainEmployee": {
                        "employeeId": 117,
                        "role": null
                    },
                    "employees":[
                        {
                            "employeeId": 28,
                            "role": null
                        },
                        {
                            "employeeId": 18,
                            "role": null
                        }
                    ],
                    "comment": "Wir wissen noch nichts",
                    "startDate": "2023-12-12",
                    "estimatedEndDate": "2023-12-30",
                    "customerId": 3
                }
                """;

        final var contentAsString = this.mockMvc.perform(post("/project/").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    
    @Test
    @WithMockUser(roles = "user")
    void deleteProject() throws Exception{
        var project1 = new ProjectEntity();
        var employee1 = new EmployeeEntity(117L, null, null);
        project1.setDescription("Eine Beschreibung");
        project1.setMainEmployee(employee1);
        project1.setInvolvedEmployees(null);
        project1.setCustomer(null);
        project1.setComment("Ein Kommentar");
        project1.setStartDate(new Date(2022, 1, 1));
        project1.setEstimatedEndDate(new Date(2023, 1, 1));

        var project2 = new ProjectEntity();
        var employee2 = new EmployeeEntity(711L, null, null);
        project2.setDescription("Eine zweite Beschreibung");
        project2.setMainEmployee(employee2);
        project2.setInvolvedEmployees(null);
        project2.setCustomer(null);
        project2.setComment("Ein zweiter Kommentar");
        project2.setStartDate(new Date(2022, 1, 1));
        project2.setEstimatedEndDate(new Date(2023, 1, 1));

        employeeRepository.save(employee1);
        projectRepository.save(project1);
        employeeRepository.save(employee2);
        projectRepository.save(project2);

        final var contentAsString = this.mockMvc.perform(delete("/project/1")
                        .header("Authorization", bearerToken))
                .andExpect(status().is2xxSuccessful());
    }
}
