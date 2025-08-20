package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerPaymentDTO class represents a Data Transfer Object for customer payments.
 */
@Data
@NoArgsConstructor
public class CustomerPaymentDTO {
    private Integer paymentnr;
    private Integer ordernr;
    private LocalDateTime paymentdate;
    private Integer paymentmethod;
    private BigDecimal amount;
}