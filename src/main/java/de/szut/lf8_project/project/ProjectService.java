package de.szut.lf8_project.project;


import de.szut.lf8_project.employee.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository repository;
    EmployeeService employeeService;

    /**
     * constructor
     * @param repository the project repository
     * @param employeeService the employee service
     */
    public ProjectService(ProjectRepository repository, EmployeeService employeeService){
        this.repository = repository;
        this.employeeService = employeeService;
    }

    /**
     * saves a project entity
     * @param entity the project entity
     * @return the project entity
     */
    public ProjectEntity save(ProjectEntity entity){
        entity = repository.save(entity);
        return entity;
    }

    public List<ProjectEntity> readAll(){
        return repository.findAll();
    }

    public ProjectEntity readById(Long id){
        var opt = repository.findById(id);
        if (opt.isEmpty()){
            return null;
        }
        return opt.get();
    }

    //DEPRECATED
//    public Project readById(Long id, String token){
//        Optional<ProjectEntity> opt = this.repository.findById(id);
//        if (opt.isEmpty()) {
//            throw new ResourceNotFoundException("Projekt mit ID " + id +" wurde nicht gefunden.");
//        }
//        return toManagementEntity(opt.get(), token);
//    }

    /**
     *     //DEPRECATED AND NOT LONGER IN USE
     *     reads Projects by employeed id
     * @param employeeId the employee id
     * @return  list of projects
     */
    public List<ProjectEntity> readByEmployeeId(Long employeeId){
        return this.repository.findByMainEmployee(employeeId);

    }

    //DEPRECATED
//    public ProjectEntity update(ProjectManagementEntity project, String token){
//        ProjectManagementEntity entity = this.readById(project.getId(), token);
//        entity.setDescription(project.getDescription());
//        //
//        entity.setCustomer(project.getCustomer());
//        entity.setComment(project.getComment());
//        entity.setStartDate(project.getStartDate());
//        entity.setEstimatedEndDate(project.getEstimatedEndDate());
//        entity.setFinalEndDate(project.getFinalEndDate());
//
//        return this.repository.save(entity);
//    }

    /**
     * deletes a project entity
     * @param entity the Project entity
     */
    public void delete(ProjectEntity entity) {
        this.repository.delete(entity);
    }


}
