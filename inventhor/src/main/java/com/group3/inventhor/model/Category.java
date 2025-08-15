package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Tatiana Fløisbonn
 *
 * The Category class represents a category entity in the Inventhor application.
 * It is used to categorize items in the inventory.
 *
 * @Entity annotation indicates that this class is a JPA entity,
 * meaning it represents a table in the database.
 * @Data annotation from Lombok generates getter and setter methods for all fields in the class.
 * @Table annotation specifies the name of the table in the database that this class is associated with,
 * and the schema in which the table resides, in this case, "inventhor".
 */
@Entity
@Data
@Table(name = "category", schema = "inventhor")
public class Category {

    // Unique identifier for the category, automatically generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categorynr;

    // Name of the category
    private String name;
}
