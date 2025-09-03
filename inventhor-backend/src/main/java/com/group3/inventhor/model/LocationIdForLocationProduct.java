package com.group3.inventhor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


/**
 * @author Tatiana Fløisbonn
 *
 * The LocationIdForLocationProduct class represents a composite primary key for the LocationProduct entity.
 * It is used to uniquely identify a location in the warehouse system based on warehouse number, rack number, and place number.
 *
 * @Embeddable indicates that this class can be embedded in another entity as a composite key.
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class.
 *
 * @Column is used to specify the column mapping for the fields in the database.
 */
@Embeddable
@Data
public class LocationIdForLocationProduct {

    @ManyToOne
    @JoinColumn(name = "warehousenr", nullable = false) // Foreign key to Warehouse table
    private Warehouse warehouse;

    @Column(name = "racknr")
    private Integer racknr;

    @Column(name = "placenr")
    private Integer placenr;
}
