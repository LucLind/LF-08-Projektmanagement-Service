package de.szut.lf8_project.employee;

import de.szut.lf8_project.project.ProjectEntity;
import de.szut.lf8_project.qualification.QualificationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="employee")
public class EmployeeEntity {
    @Id
    private Long id;
//    @OneToOne(mappedBy = "mainEmployee",
//            fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "mainEmployee",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ProjectEntity> mainProject;

    @ManyToMany(mappedBy = "involvedEmployees", cascade = CascadeType.MERGE)
    private Set<ProjectEntity> involvedProjects;

    public EmployeeEntity(Employee e){
        this.id = e.getId();
        e.setEntity(this);
    }
}
