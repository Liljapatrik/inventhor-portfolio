package com.group3.inventhor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Data Transfer Object (DTO) representing a warehouse order.
 * Contains details such as order number, warehouse and supplier information,
 * order and delivery dates, status, and related nested DTOs for supplier, warehouse, and order status.
 *
 * Author: Nils Patrik Lilja
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseOrderDTO {
    private Integer ordernr;
    private Integer warehousenr;
    private String name;
    private Integer suppliernr;
    private LocalDateTime orderdate;
    private Integer statusnr;
    private LocalDateTime deliverydate;
    private String suppliername;
    private String orderstatusname;

    private SupplierDTO supplier;
    private WarehouseDTO warehouse;
    private OrderStatusDTO orderStatus;
}
