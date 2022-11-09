package de.szut.lf8_project.employee;

import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.project.ProjectEntity;
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
     * Liest aus aud der Datenbank und valdiert mit Mitarbeiter dienst
     * @param mainEmployeeId Die ID des Hauptverantwortlichen Mitarbeiters
     * @return Mitarbeiter mit gewählter ID
     */
    public EmployeeEntity readById(Long mainEmployeeId) {
        Optional<EmployeeEntity> opt = this.repository.findById(mainEmployeeId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException("Mitarbeiter mit ID " + mainEmployeeId +" wurde nicht gefunden.");
        }
        return opt.get();
    }

    /**
     * Liest aus aud der Datenbank und valdiert mit Mitarbeiter dienst
     * @param employeeIds Die IDs der Mitarbeiter
     * @return Mitarbeiter mit gewählten IDs
     */
    public Set<EmployeeEntity> readById(Set<Long> employeeIds) {
        // TODO
        return new HashSet<EmployeeEntity>();
    }

    public List<EmployeeEntity> findBySkill(String skill) {
        return this.repository.findBySkill(skill);
    }

    public boolean employeeIsValid(Long id){
        // TODO check if the employee is valid
        return false;
    }


    public boolean employeeHasSkill(Long id,String skill){
        // TODO check if the employee has the qualification
        return true;
    }
}
