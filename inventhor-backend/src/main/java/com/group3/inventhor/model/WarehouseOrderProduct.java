package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents the association between a warehouse order and its products.
 *
 * This entity maps to the "warehouseorderproduct" table in the "inventhor" schema.
 * It uses a composite primary key represented by WarehouseOrderProductId.
 * Each instance links a specific product to a specific warehouse order,
 * along with details such as quantity ordered and purchase price.
 *
 * Fields:
 * - id: Composite key consisting of order number and product number.
 * - product: Many-to-one relationship to the Product entity.
 * - warehouseOrder: Many-to-one relationship to the WarehouseOrder entity.
 * - quantity: Number of units ordered for the product.
 * - buyprice: Purchase price per unit for the product.
 *
 * Author: Nils Patrik Lilja
 */
@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouseorderproduct", schema = "inventhor")
public class WarehouseOrderProduct {

    @EmbeddedId
    private WarehouseOrderProductId id;

    @ManyToOne
    @MapsId("productnr")
    @JoinColumn(name = "productnr", referencedColumnName = "productnr")
    private Product product;

    @ManyToOne
    @MapsId("ordernr")
    @JoinColumn(name = "ordernr", referencedColumnName = "ordernr")
    private WarehouseOrder warehouseOrder;

    private Integer quantity;
    private BigDecimal buyprice;

}
