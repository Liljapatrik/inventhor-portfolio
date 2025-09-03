package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @Author Steewen Dennis Chanavi Holden
 */
@Entity
@Getter
@Setter
@Table(name = "customerorder", schema = "inventhor")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ordernr;
    private LocalDateTime orderdate;
    private LocalDateTime deliverydate;

    // Relationship to CustomerOrderProduct
    @OneToMany(mappedBy = "customerOrder", fetch = FetchType.LAZY)
    private Set<CustomerOrderProduct> customerOrderProducts;

    // Relationship to Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customernr", updatable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "statusnr")
    private OrderStatus status;


}