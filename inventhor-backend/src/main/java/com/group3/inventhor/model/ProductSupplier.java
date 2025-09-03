package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing the association between a Product and a Supplier.
 *
 * Uses a composite primary key defined by ProductSupplierId, consisting of:
 * - productnr: the product identifier
 * - suppliernr: the supplier identifier
 *
 * The EmbeddedId annotation specifies the composite key.
 *
 * The ManyToOne relationships link to the Product and Supplier entities,
 * with MapsId ensuring the composite key fields are mapped correctly.
 *
 * This entity is mapped to the "productsupplier" table in the "inventhor" schema.
 *
 * Lombok annotations generate boilerplate code such as constructors, getters, setters, equals, and hashCode.
 *
 * Author: Nils Patrik Lilja
 */
@Entity
@Table(name = "productsupplier", schema = "inventhor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSupplier {

    @EmbeddedId
    private ProductSupplierId id;

    @ManyToOne
    @MapsId("productnr")
    @JoinColumn(name = "productnr", referencedColumnName = "productnr")
    private Product product;

    @ManyToOne
    @MapsId("suppliernr")
    @JoinColumn(name = "suppliernr", referencedColumnName = "suppliernr")
    private Supplier supplier;
}
