package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * * @author Tatiana Fløisbonn
 *
 * @Entity the annotation indicates that this class is a JPA entity.
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class, also all arguments constructor
 * @Table annotation specifies the name of the table in the database that this class is associated with.
 */
@Entity
@Data
@Table(name = "product", schema = "inventhor")
public class Product {
    // Unique identifier for the product, automatically generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productnr;
    // Fields representing product details
    private String image; // Default image URL https://img.icons8.com/?size=100&id=104948&format=png&color=000000
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "categorynr")
    private Category category; // Category to which the product belongs

    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal depth;
    private BigDecimal weight;
    private BigDecimal sellprice;
    private String unit; // Unit of measurement for the product (e.g., kg, pcs, etc.)
}
