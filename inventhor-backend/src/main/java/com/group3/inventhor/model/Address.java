package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tatiana Fløisbonn
 *
 * The Address class represents an address entity in the Inventhor application.
 *
 * @Entity annotation indicates that this class is a JPA entity.
 *      It means that it represents a table in the database.
 * @Getter and @Setter annotations from Lombok generate getter and setter methods for all fields in the class.
 * @Table annotation specifies the name of the table in the database that this class is associated with.
 *      It also specifies the schema in which the table resides, in this case, "inventhor".
 */
@Entity
@Getter
@Setter
@Table(name = "address", schema = "inventhor")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressnr;
    private String country;
    private String city;
    private String street;
    private String postcode;

    @OneToOne(mappedBy = "address")
    private Employee employee;
}