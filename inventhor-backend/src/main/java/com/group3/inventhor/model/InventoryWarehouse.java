package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entity representing the inventory levels of a specific product stored in a warehouse.
 *
 * Maps to the "inventorywarehouse" table in the "inventhor" schema.
 *
 * Composite primary key is represented by link InventoryWarehouseI and consists of productnr and warehousenr.
 *
 * Relationships:
 * - Many-to-one association to link Product, mapped by productnr.
 * - Many-to-one association to link Warehouse, mapped by warehousenr.
 *
 * Contains inventory limits for the product in the warehouse:
 * - maxstocklvl: maximum stock level allowed.
 * - minstocklvl: minimum stock level allowed.
 *
 * Lombok annotations are used to generate getters, setters, constructors, equals, hashCode, and toString methods.
 *
 * Author: Nils Patrik Lilja
 */
@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventorywarehouse", schema = "inventhor")
public class InventoryWarehouse {

    @EmbeddedId
    private InventoryWarehouseId id;

    @ManyToOne
    @MapsId("productnr")
    @JoinColumn(name = "productnr", referencedColumnName = "productnr")
    private Product product;

    @ManyToOne
    @MapsId("warehousenr")
    @JoinColumn(name = "warehousenr", referencedColumnName = "warehousenr")
    private Warehouse warehouse;

    private BigDecimal maxstocklvl;
    private BigDecimal minstocklvl;

}
