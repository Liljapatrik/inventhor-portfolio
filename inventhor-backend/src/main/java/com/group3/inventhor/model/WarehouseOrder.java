package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


/**
 * Represents a warehouse order in the system.
 *
 * This entity maps to the "warehouseorder" table in the "inventhor" schema.
 * Each WarehouseOrder is linked to a specific Warehouse and Supplier,
 * and includes information such as order date, delivery date, and status.
 *
 * Fields:
 * - ordernr: Primary key, auto-generated unique identifier for the order.
 * - warehouse: Many-to-one relationship to Warehouse entity.
 * - supplier: Many-to-one relationship to Supplier entity.
 * - orderdate: Timestamp of when the order was placed.
 * - status: Many-to-one relationship to OrderStatus entity indicating order status.
 * - deliverydate: Expected or actual delivery date of the order.
 *
 * Author: Nils Patrik Lilja
 */
@Entity
@Getter
@Setter
@Table(name = "warehouseorder", schema = "inventhor")
public class WarehouseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ordernr;

    @ManyToOne
    @JoinColumn(name = "warehousenr", referencedColumnName = "warehousenr", nullable = false)
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "suppliernr", referencedColumnName = "suppliernr", nullable = false)
    private Supplier supplier;

    @Column(name = "orderdate", nullable = false)
    private LocalDateTime orderdate;

    @ManyToOne
    @JoinColumn(name = "statusnr", referencedColumnName = "statusnr", nullable = false)
    private OrderStatus status;

    @Column(name = "deliverydate")
    private LocalDateTime deliverydate;
}
