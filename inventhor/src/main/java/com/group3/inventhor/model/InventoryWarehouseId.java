package com.group3.inventhor.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Composite primary key class for the InventoryWarehouse entity.
 *
 * This class represents the combined key consisting of:
 * - warehousenr: the warehouse identifier
 * - productnr: the product identifier
 *
 * Marked as @Embeddable for use as an embedded ID in JPA entities.
 * Implements Serializable as required for composite keys.
 *
 * Lombok annotations generate boilerplate code such as constructors, getters, setters, equals, and hashCode.
 *
 * Author: Nils Patrik Lilja
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryWarehouseId implements Serializable {

    private Integer warehousenr;
    private Integer productnr;
}
