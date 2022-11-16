package de.szut.lf8_project.role;


import org.springframework.stereotype.Service;

/**
 * ProjectEmployeeRoleService class
 */
@Service
public class ProjectEmployeeRoleService {
    private final ProjectEmployeeRoleRepository repository;

    /**
     * constructor
     * @param repository the ProjectEmployeeRoleRepository
     */
    public ProjectEmployeeRoleService(ProjectEmployeeRoleRepository repository){
        this.repository = repository;
    }

    /**
     * saves the ProjectEmployeeRoleEntity
     * @param entity the ProjectEmployeeRoleEntity
     * @return the saved ProjectEmployeeRoleEntity
     */
    public ProjectEmployeeRoleEntity save(ProjectEmployeeRoleEntity entity){
        return repository.save(entity);
    }
    /**
     * deletes the ProjectEmployeeRoleEntity
     * @param entity the ProjectEmployeeRoleEntity
     */
    public void delete(ProjectEmployeeRoleEntity entity){
        repository.delete(entity);
    }

}
