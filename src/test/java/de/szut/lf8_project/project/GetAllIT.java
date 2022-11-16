package de.szut.lf8_project.project;

import de.szut.lf8_project.customer.CustomerEntity;
import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.hello.HelloEntity;
import de.szut.lf8_project.role.ProjectEmployeeRoleEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetAllIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/project/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void findAll() throws Exception {

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

        final var contentAsString = this.mockMvc.perform(get("/project")
                        .header("Authorization", bearerToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description", is("Eine Beschreibung")))
                .andExpect(jsonPath("$[1].description", is("Eine zweite Beschreibung")));
    }
}
