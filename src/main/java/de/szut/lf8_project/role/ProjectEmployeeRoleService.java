package de.szut.lf8_project.role;


import org.springframework.stereotype.Service;

@Service
public class ProjectEmployeeRoleService {
    private final ProjectEmployeeRoleRepository repository;

    public ProjectEmployeeRoleService(ProjectEmployeeRoleRepository repository){
        this.repository = repository;
    }

    public ProjectEmployeeRoleEntity save(ProjectEmployeeRoleEntity entity){
        return repository.save(entity);
    }
    public void delete(ProjectEmployeeRoleEntity entity){
        repository.delete(entity);
    }

}
