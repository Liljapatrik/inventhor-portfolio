package com.group3.inventhor.model;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author Tatiana Fløisbonn
 *
 * The LocationProduct class represents a many-to-many relationship between locations and products in the Inventhor application.
 * It contains an embedded ID that combines the location and product identifiers, along with a quantity field to represent the amount of the product at that location.
 *
 * @Entity annotation indicates that this class is a JPA entity.
 * @Data annotation is a Lombok annotation that generates getter and setter methods, as well as equals, hashCode, and toString methods.
 * @Table annotation specifies the name of the database table and schema where this entity will be stored.
 *
 * @EmbeddedId is used to define a composite primary key for the entity, which is represented by the LocationProductId class.
 */
@Entity
@Data
@Table(name = "locationproduct", schema = "inventhor")
public class LocationProduct {

    @EmbeddedId
    private LocationProductId locationProductId;

    private BigDecimal quantity;

}
