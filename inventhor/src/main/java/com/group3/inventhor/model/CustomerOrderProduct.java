package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * join table between CustomerOrder and Product.
 * It contains the quantity of each product in an order.
 */
@Entity
@Getter
@Setter
@Table(name = "customerorderproduct", schema = "inventhor")
@IdClass(CustomerOrderProductId.class)
public class CustomerOrderProduct implements Serializable {

    @Id
    @Column(name = "ordernr")
    private Integer ordernr;

    @Id
    @Column(name = "productnr")
    private Integer productnr;

    @Id
    @Column(name = "warehousenr")
    private Integer warehousenr;

    private Integer quantity;

    // Relationship to CustomerOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordernr", insertable = false, updatable = false)
    private CustomerOrder customerOrder;

    // Relationship to Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productnr", insertable = false, updatable = false)
    private Product product;

    // Relationship to Warehouse
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehousenr", insertable = false, updatable = false)
    private Warehouse warehouse;
}