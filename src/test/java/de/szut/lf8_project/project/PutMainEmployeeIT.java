package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PutMainEmployeeIT extends AbstractIntegrationTest {
    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/project/1/mainEmployee/9/qualification/weise"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void putEmployee() throws Exception {

        var project1 = new ProjectEntity();
        var employee1 = new EmployeeEntity(9L, null, null);
        project1.setDescription("Eine Beschreibung");
        project1.setMainEmployee(null);
        project1.setInvolvedEmployees(null);
        project1.setCustomer(null);
        project1.setComment("Ein Kommentar");
        project1.setStartDate(new Date(2022, 1, 1));
        project1.setEstimatedEndDate(new Date(2023, 1, 1));

        employeeRepository.save(employee1);
        projectRepository.save(project1);

        final var contentAsString = this.mockMvc.perform(put("/project/1/mainEmployee/9/qualification/weise")
                        .header("Authorization", bearerToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("mainEmployee", is(9)));
    }
}