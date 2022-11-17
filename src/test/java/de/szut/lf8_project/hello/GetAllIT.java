package de.szut.lf8_project.hello;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetAllIT extends AbstractIntegrationTest {


    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/hello/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void findAll() throws Exception {

        helloRepository.save(new HelloEntity("Foo"));
        helloRepository.save(new HelloEntity("Bar"));

        final var contentAsString = this.mockMvc.perform(get("/hello/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].message", is("Foo")))
                .andExpect(jsonPath("$[1].message", is("Bar")));
    }

}
