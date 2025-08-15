package com.group3.inventhor.model;


import jakarta.persistence.*;
import lombok.Data;


/**
 * @author Tatiana Fløisbonn
 *
 * The LocationProductId class represents a composite primary key for the LocationProduct entity in the Inventhor application.
 * It is used to uniquely identify a product in a specific location.
 *
 * @Embeddable annotation indicates that this class can be embedded in another entity.
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class.
 *
 * @Embedded annotation is used to indicate that the LocationIdForLocationProduct field is an embedded object.
 */
@Embeddable
@Data
public class LocationProductId {

    @Embedded
    private LocationIdForLocationProduct locationIdForLocationProduct;

    @ManyToOne
    @JoinColumn(name = "productnr", nullable = false)
    private Product product;

}
