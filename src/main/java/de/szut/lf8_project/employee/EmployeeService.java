package de.szut.lf8_project.employee;

import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    /**
     * Liest aus aud der datenbank und valdiert mit mitarbeiter dienst
     * @param mainEmployeeId
     * @return
     */
    public EmployeeEntity readById(Long mainEmployeeId) {
        Optional<EmployeeEntity> opt = this.repository.findById(mainEmployeeId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException("Mitarbeiter mit ID " + mainEmployeeId +" wurde nicht gefunden.");
        }
        return opt.get();
    }
    public Set<EmployeeEntity> readById(Set<Long> mainEmployeeId) {
        // TODO
        return new HashSet<EmployeeEntity>();
    }

    public List<EmployeeEntity> findBySkill(String skill) {
        return this.repository.findBySkill(skill);
    }
}
