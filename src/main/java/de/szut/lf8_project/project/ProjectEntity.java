package de.szut.lf8_project.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    //private Employee mainEmployee;
    //private List<Employee> employees;
    // private Customer customer;
    private String comment;
    private Date startDate;
    private Date estimatedEndDate;
    private Date finalEndDate;


}
