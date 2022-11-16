package de.szut.lf8_project.project;

import de.szut.lf8_project.customer.CustomerEntity;
import de.szut.lf8_project.employee.EmployeeEntity;
import de.szut.lf8_project.employee.Employee;
import de.szut.lf8_project.role.ProjectEmployeeRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Project entity class
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

//    @OneToOne(fetch = FetchType.EAGER,
//            cascade = CascadeType.ALL)

    @ManyToOne
    private EmployeeEntity mainEmployee;
    private String mainEmployeeQualification;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectEmployeeRoleEntity> involvedEmployees;
    @ManyToOne
    private CustomerEntity customer;
    private String comment;
    private Date startDate;
    private Date estimatedEndDate;
    private Date finalEndDate;

}
