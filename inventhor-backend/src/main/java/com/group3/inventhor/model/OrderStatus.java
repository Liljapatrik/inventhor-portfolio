package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Data;


/**
 * @author Tatiana Fløisbonn
 *
 * The OrderStatus class represents the status of an order in the Inventhor application.
 *
 * @Data annotation from Lombok generates getter and setter methods for all fields in the class.
 * @Entity annotation indicates that this class is a JPA entity, meaning it represents a table in the database.
 * @Table annotation specifies the name of the table in the database that this class is associated with,
 * and the schema in which the table resides, in this case, "inventhor".
 */
@Entity
@Data
@Table(name = "orderstatus", schema = "inventhor")
public class OrderStatus {

    // Unique identifier for the order status, automatically generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statusnr;

    // Name of the order status
    private String name;
}
