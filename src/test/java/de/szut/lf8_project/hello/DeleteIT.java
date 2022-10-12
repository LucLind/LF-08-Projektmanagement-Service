package de.szut.lf8_project.hello;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteIT extends AbstractIntegrationTest {


    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(delete("/hello/4"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {
        HelloEntity stored = helloRepository.save(new HelloEntity("Foo"));

        final var content = this.mockMvc.perform(
                        delete("/hello/" + stored.getId()))
                .andExpect(status().isNoContent());
        assertThat(helloRepository.findById(stored.getId()).isPresent()).isFalse();
    }

    @Test
    @WithMockUser(roles = "user")
    void idDoesNotExist() throws Exception {
        final var contentAsString = this.mockMvc.perform(delete("/hello/5"))
                .andExpect(content().string(containsString("HelloEntity not found on id = 5")))
                .andExpect(status().isNotFound());
    }


}
