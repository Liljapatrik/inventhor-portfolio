package com.group3.inventhor.dto;

import com.group3.inventhor.model.WarehouseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * Data Transfer Object (DTO) representing a product included in a warehouse order.
 * Contains product details such as product number, name, quantity ordered, purchase price,
 * and references to the related warehouse order and product entities.
 *
 * Author: Nils Patrik Lilja
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseOrderProductDTO {
    private Integer ordernr;
    private Integer productnr;
    private String name;
    private Integer quantity;
    private BigDecimal buyprice;


    private WarehouseOrder warehouse;
    private ProductDTO product;

}
