package de.szut.lf8_project.project;

import de.szut.lf8_project.customer.CustomerEntity;
import de.szut.lf8_project.employee.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private EmployeeEntity mainEmployee;
//    @ManyToMany
//    private Set<EmployeeEntity> employees;
    @ManyToOne
    private CustomerEntity customer;
    private String comment;
    private Date startDate;
    private Date estimatedEndDate;
    private Date finalEndDate;


}
