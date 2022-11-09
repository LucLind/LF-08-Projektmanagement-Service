package de.szut.lf8_project.project;

<<<<<<< HEAD
import de.szut.lf8_project.hello.HelloEntity;
=======
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
>>>>>>> 74becc48c1f490c30883874a86844c951efed003
import de.szut.lf8_project.project.ProjectEntity;
import de.szut.lf8_project.project.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository){
        this.repository = repository;
    }

    public ProjectEntity create(ProjectEntity entity){
        this.repository.save(entity);
        return null;
    }

    public List<ProjectEntity> readAll(){
        return this.repository.findAll();
    }

    public ProjectEntity readById(Long id){
        Optional<ProjectEntity> opt = this.repository.findById(id);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException("Projekt mit ID " + id +" wurde nicht gefunden.");
        }
        return opt.get();
    }

    public List<ProjectEntity> readByEmployeeId(Long employeeId){
        return this.repository.findByEmployeesId(employeeId);

    }

    public ProjectEntity update(ProjectEntity entity){
        // TODO
        return null;
    }

    public void delete(ProjectEntity entity) {

        this.repository.delete(entity);
    }
}
