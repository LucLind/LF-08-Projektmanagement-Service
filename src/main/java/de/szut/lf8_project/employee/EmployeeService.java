package de.szut.lf8_project.employee;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository){
        this.repository = repository;
    }

    public List<EmployeeEntity> readByProjectId(Long projectId){
        // TODO
        return null;
    }

    public EmployeeEntity readById(Long mainEmployeeId) {
        // TODO
        return null;
    }
    public Set<EmployeeEntity> readById(Set<Long> mainEmployeeId) {
        // TODO
        return null;
    }
}
