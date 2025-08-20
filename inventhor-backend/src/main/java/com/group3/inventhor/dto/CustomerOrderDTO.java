package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerOrderDTO class represents a Data Transfer Object for customer orders.
 */

@Data
@NoArgsConstructor
public class CustomerOrderDTO {
    private Integer ordernr;
    private CustomerDTO customer;
    private LocalDateTime orderdate;
    private OrderStatusDTO status;
    private LocalDateTime deliverydate;
}