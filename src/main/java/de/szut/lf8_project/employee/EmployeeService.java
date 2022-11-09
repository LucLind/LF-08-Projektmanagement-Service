package de.szut.lf8_project.employee;

import com.google.gson.Gson;
import de.szut.lf8_project.employee.dto.GetEmployeeDto;
import de.szut.lf8_project.qualification.QualificationMapper;
import org.keycloak.adapters.BearerTokenRequestAuthenticator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {
    private static final String EMPLOYEE_SERVICE_URL = "https://employee.szut.dev/employees";
    private final RestTemplate restTemplate;
    //private final EmployeeRepository repository;

    public EmployeeService(){
        //this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public List<EmployeeEntity> readByProjectId(Long projectId){
        // TODO
        return null;
    }

    /**
     * Liest aus aud der datenbank und valdiert mit mitarbeiter dienst
     * @param mainEmployeeId
     * @return
     */
    public EmployeeEntity readById(Long mainEmployeeId, String token) {
        URL url = null;
        try
        {
            url = new URL(EMPLOYEE_SERVICE_URL + "/" + mainEmployeeId);
            HttpURLConnection MyConn = (HttpURLConnection) url.openConnection();
            MyConn.setRequestMethod(HttpMethod.GET.name());
            MyConn.setRequestProperty(HttpHeaders.AUTHORIZATION, token);

            int responseCode = MyConn.getResponseCode();
            if (responseCode == MyConn.HTTP_OK) {
                // Create a reader with the input stream reader.
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        MyConn.getInputStream()));
                String inputLine;

                // Create a string buffer
                StringBuffer response = new StringBuffer();

                // Write each of the input line
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                GetEmployeeDto dto = new Gson().fromJson(response.toString(), GetEmployeeDto.class);
                //Show the output
                System.out.println(response.toString());

                return EmployeeMapper.GetEmployeeDtoToEmployeeEntity(dto);
            } else {
                System.out.println("Error found !!!");
            }

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity http = new HttpEntity(headers);
        ResponseEntity response = this.restTemplate
                .exchange(
                        EMPLOYEE_SERVICE_URL+"/{id}",
                        HttpMethod.GET, http, String.class, mainEmployeeId);

        //var entity = EmployeeMapper.GetEmployeeDtoToEmployeeEntity(response.getBody());
        return null;
    }
    public Set<EmployeeEntity> readById(Set<Long> mainEmployeeId) {
        // TODO
        return new HashSet<EmployeeEntity>();
    }

//    public List<EmployeeEntity> findBySkill(String skill) {
//        return this.repository.findBySkill(skill);
//    }
}
