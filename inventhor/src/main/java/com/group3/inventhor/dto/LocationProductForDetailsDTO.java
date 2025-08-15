package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Tatiana Fløisbonn
 *
 * The LocationProductForDetailsDTO class represents a Data Transfer Object for detailed information about products located in a specific warehouse location.
 * It contains information about the warehouse, rack number, place number, product details, and the quantity of the product at that location.
 * This DTO is used to transfer detailed data related to product locations within the inventory system.
 *
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class, as well as a constructor with all arguments.
 * @NoArgsConstructor generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class LocationProductForDetailsDTO {
    private WarehouseDTO warehouse;
    private Integer racknr;
    private Integer placenr;
    private ProductDTO product;
    private BigDecimal quantity;
}
