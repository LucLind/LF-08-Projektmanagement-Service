package de.szut.lf8_project.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PostIT extends AbstractIntegrationTest {

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
    void storeAndFind() throws Exception{
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
        final var contentAsString = this.mockMvc.perform(post("/project/").content(content).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", bearerToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("description", is("Unser erstes Projekt")))
                .andExpect(jsonPath("mainEmployee").exists())
                .andExpect(jsonPath("employees").isArray())
                .andExpect(jsonPath("comment", is("Wir wissen noch nichts")))
                .andExpect(jsonPath("startDate", is("2023-12-12T00:00:00.000+00:00")))
                .andExpect(jsonPath("estimatedEndDate", is("2023-12-30T00:00:00.000+00:00")))
                .andExpect(jsonPath("customer", is(357))) // CustomerId wird nicht gespeichert und im GetDTO ist immer 357
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
