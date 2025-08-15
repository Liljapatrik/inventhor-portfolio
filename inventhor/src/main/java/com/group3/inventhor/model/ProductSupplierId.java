package com.group3.inventhor.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Composite primary key class for the ProductSupplier entity.
 *
 * Represents the combined key of productnr (product ID) and suppliernr (supplier ID).
 *
 * This class must implement Serializable as required for composite keys in JPA.
 *
 * It is annotated with @Embeddable to indicate it is an embedded primary key.
 *
 * Lombok annotations generate constructors, getters, setters, equals, and hashCode.
 *
 * Author: Nils Patrik Lilja
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSupplierId implements Serializable {

    private Integer productnr;
    private Integer suppliernr;
}
