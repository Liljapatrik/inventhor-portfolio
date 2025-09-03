package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerOrderProductDTO class represents a Data Transfer Object for customer products in orders.
 */

@Data
@NoArgsConstructor
public class CustomerOrderProductDTO {
    private CustomerOrderDTO customerorder;
    private ProductDTO product;
    private WarehouseDTO warehouse;
    private Integer quantity;
}

