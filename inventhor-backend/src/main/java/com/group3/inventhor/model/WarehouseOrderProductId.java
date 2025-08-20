package com.group3.inventhor.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Composite primary key class for WarehouseOrderProduct entity.
 *
 * Consists of:
 * - ordernr: The ID of the warehouse order.
 * - productnr: The ID of the product.
 *
 * This class is marked as @Embeddable to be used as an embedded ID in the
 * WarehouseOrderProduct entity.
 *
 * Implements Serializable as required for JPA composite keys.
 *
 * Author: Nils Patrik Lilja
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseOrderProductId implements Serializable {

    private Integer ordernr;
    private Integer productnr;
}
