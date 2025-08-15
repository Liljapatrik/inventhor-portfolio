package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Tatiana Fløisbonn
 *
 * The PaymentMethod class represents a payment method entity in the Inventhor application.
 * It is used to define different payment methods available in the system.
 *
 * @Entity annotation indicates that this class is a JPA entity,
 * meaning it represents a table in the database.
 * @Data annotation from Lombok generates getter and setter methods for all fields in the class.
 * @Table annotation specifies the name of the table in the database that this class is associated with,
 * and the schema in which the table resides, in this case, "inventhor".
 */
@Entity
@Data
@Table(name = "paymentmethod", schema = "inventhor")
public class PaymentMethod {

    // Unique identifier for the payment method, automatically generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentmethodnr;

    // Name of the payment method
    private String name;
}
