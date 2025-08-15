package com.group3.inventhor.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tatiana Fløisbonn
 *
 * The Customer class represents a customer entity in the system.
 *
 * @Entity annotation indicates that this class is a JPA entity, meaning it will be mapped to a table in the database.
 * @Getter and @Setter annotations from Lombok generate getter and setter methods for all fields in the class.
 * @Table annotation specifies the name of the table in the database that this class is associated with.
 *
 */
@Entity // means there will be a table in the database called employee where all the employee details are stored.
@Getter
@Setter
@Table(name = "customer", schema = "inventhor") //specify the name of the table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customernr;

    private String email;
    private String phone;
    private String firstname;
    private String lastname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressnr")
    private Address address;

}
