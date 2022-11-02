package de.szut.lf8_project.project;

import de.szut.lf8_project.employee.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private EmployeeEntity mainEmployee;
    @ManyToMany
    private List<EmployeeEntity> employees;
    //private Customer customer;
    private String comment;
    private Date startDate;
    private Date estimatedEndDate;
    private Date finalEndDate;


}
