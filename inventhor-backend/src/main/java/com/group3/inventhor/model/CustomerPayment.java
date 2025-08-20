package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerPayment class represents a payment made by a customer for an order.
 */
@Entity
@Getter
@Setter
@Table(name = "customerpayment", schema = "inventhor")
public class CustomerPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentnr;

    @Column(name = "ordernr")
    private Integer ordernr;
    private LocalDateTime paymentdate;

    @Column(name = "paymentmethod")
    private Integer paymentmethod;
    private BigDecimal amount;
}