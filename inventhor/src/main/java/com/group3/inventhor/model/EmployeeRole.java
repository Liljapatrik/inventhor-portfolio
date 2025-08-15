package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Tatiana Fløisbonn
 *
 * The Picture class represents a picture entity in the Inventhor application.
 *
 * @Entity annotation indicates that this class is a JPA entity, meaning it will be mapped to a table in the database.
 * @Data annotation from Lombok generates getter and setter methods for all fields in the class, as well as equals, hashCode, and toString methods.
 * @Table annotation specifies the name of the table in the database that this class is associated with.
 */
@Entity
@Data
@Table(name = "employeerole", schema = "inventhor")
public class EmployeeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rolenr;

    private String name; // we have two roles "admin" and "staff" which are used to determine the privileges of the employee in the system
}
