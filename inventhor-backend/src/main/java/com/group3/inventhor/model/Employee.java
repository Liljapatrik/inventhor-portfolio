package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Tatiana Fløisbonn
 *
 * Employee class represents an employee entity in the Inventhor application.
 *
 * @Entity the annotation indicates that this class is a JPA entity.
 *     It means that it represents a table in the database.
 * @Getter and @Setter annotations from Lombok generate getter and setter methods for all fields in the class.
 * @Table annotation specifies the name of the table in the database that this class is associated with.
 *     It also specifies the schema in which the table resides, in this case, "inventhor".
 */
@Entity
@Getter
@Setter
@Table(name = "employee", schema = "inventhor") //specify the name of the table
public class Employee {
    // Unique identifier for the employee, automatically generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeenr;
    // Fields representing employee details
    private String email;
    private String phone;
    private String passwordhash;
    private String firstname;
    private String lastname;
    private String position; // position declare position in company

    @ManyToOne
    @JoinColumn(name = "rolenr")
    private EmployeeRole role;
    private LocalDate employeddate;

    @Column(name = "isactive", insertable = false)
    private boolean isactive; // isactive indicates if the employee is currently working or not
    private String image; // Default image URL https://img.icons8.com/?size=100&id=104948&format=png&color=000000

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressnr")
    private Address address;
}
