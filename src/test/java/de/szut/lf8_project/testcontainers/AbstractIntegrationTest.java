package de.szut.lf8_project.testcontainers;

import de.szut.lf8_project.employee.EmployeeRepository;
import de.szut.lf8_project.hello.HelloRepository;
import de.szut.lf8_project.project.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * A fast slice test will only start jpa context.
 * <p>
 * To use other context beans use org.springframework.context.annotation.@Import annotation.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
@ContextConfiguration(initializers = PostgresContextInitializer.class)
public class AbstractIntegrationTest {

    /**
     * The generated bearer token has to be inserted in here in order to test the endpoints
     */
    protected final String bearerToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIzUFQ0dldiNno5MnlQWk1EWnBqT1U0RjFVN0lwNi1ELUlqQWVGczJPbGU0In0.eyJleHAiOjE2Njg1OTMzMTcsImlhdCI6MTY2ODU4OTcxNywianRpIjoiOWQ2OWM3MjQtMTAwNS00YzlkLTg3NTQtMTdiY2IwZTAwOGE3IiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay5zenV0LmRldi9hdXRoL3JlYWxtcy9zenV0IiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjU1NDZjZDIxLTk4NTQtNDMyZi1hNDY3LTRkZTNlZWRmNTg4OSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImVtcGxveWVlLW1hbmFnZW1lbnQtc2VydmljZSIsInNlc3Npb25fc3RhdGUiOiIwZTdmYzNhNi03YTgxLTRkMzktYWU2NS04ZDI1NTI3NTA3MDAiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1zenV0IiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIn0.EoN9jlxUGmUxpdpjjDIqI-tCtjRtA_gdE_TKhXX1KHu0JkSJWplOW5q7TBmQUfGpwzBZpojX0swHq0hUft80Xmz-Dbe7xy3ibHTVeqwODsZvIjHNH7d3J-ucekjgLdIzJAkUtX8bzJjdYrd9ugLoDGK_vIXYRBIIejs2uU0sc6x-evZwEGDILHNdP2qlCGylQHxQmH1kB6nfw9DW_EBVHH8doRhewLuM44MuqbAqtreW7H5WquAt_SEBuoEYmEU0RiQckccJRb7eyYiYqsF5A5LwpOABk6N5dyTu2OsHzyD6Y3N6YL6Vir3j0VQmgTf25d22aTFf6L_FAzjKCNtYTw";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected HelloRepository helloRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        helloRepository.deleteAll();
    }
}
