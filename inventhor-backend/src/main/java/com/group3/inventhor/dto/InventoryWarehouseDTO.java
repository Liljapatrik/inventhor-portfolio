package com.group3.inventhor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Nils Patrik Lilja
 */

/**
 * Data Transfer Object (DTO) representing the inventory information
 * of a product stored in a warehouse.
 * Contains stock level limits and references to the warehouse and product details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryWarehouseDTO {
    private Integer warehousenr;
    private Integer productnr;
    private BigDecimal maxstocklvl;
    private BigDecimal minstocklvl;

    private WarehouseDTO warehouse;
    private ProductDTO product;

}
