package de.szut.lf8_project.employee;

import com.google.gson.Gson;
import de.szut.lf8_project.employee.dto.GetEmployeeDto;
import de.szut.lf8_project.helper.JsonHelper;
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

            var dto = JsonHelper.getDTOFromConnection(GetEmployeeDto.class, MyConn);
            if (dto != null){
                return EmployeeMapper.GetEmployeeDtoToEmployeeEntity(dto);
            }

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
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
