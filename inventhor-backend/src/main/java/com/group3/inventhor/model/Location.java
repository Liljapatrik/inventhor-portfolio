package com.group3.inventhor.model;


import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Furo Muktar Eshetu
 *
 * The Location class represents a location within a warehouse in the Inventhor application.
 */
@Entity
@Data
@Table(name = "location", schema = "inventhor")
public class Location {

    @EmbeddedId
    private LocationId locationId;
}
