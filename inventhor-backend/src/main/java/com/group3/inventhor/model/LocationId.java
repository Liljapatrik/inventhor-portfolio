package com.group3.inventhor.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * @author Furo Muktar Eshetu
 *
 * The LocationId class represents a composite primary key for the Location entity.
 * It is used to uniquely identify a location within a warehouse in the Inventhor application.
 *
 * @Embeddable indicates that this class can be embedded in another entity as a composite key.
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class.
 */
@Embeddable
@Data
public class LocationId {

    @ManyToOne
    @JoinColumn(name = "warehousenr", nullable = false) // Foreign key to Warehouse table
    private Warehouse warehouse;

    private Integer racknr;
    private Integer placenr;
}
