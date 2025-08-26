package com.group3.inventhor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

/**
 * @author Nils Patrik Lilja
 */

/**
 * Data Transfer Object (DTO) used for creating a warehouse order.
 * Contains details about the warehouse, supplier, delivery date, order status, and list of ordered products.
 * The nested ProductLine class represents individual products within the order, including product ID,
 * quantity, and purchase price.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseOrderCreateDTO {
    private Integer warehousenr;
    private Integer suppliernr;
    private LocalDateTime deliverydate;
    private String orderstatusname;
    private List<ProductLine> products;

    

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductLine{
        private Integer productnr;
        private Integer quantity;
        private BigDecimal buyprice;
    }
}
