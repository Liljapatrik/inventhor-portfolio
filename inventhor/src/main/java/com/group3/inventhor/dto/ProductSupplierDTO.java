package com.group3.inventhor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Nils Patrik Lilja
 */

/**
 * Data Transfer Object (DTO) representing a product supplied by a supplier.
 * Contains product details including identification, category, image, and pricing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSupplierDTO {

    private Integer productnr;
    private String productname;
    private Integer suppliernr;
    private String productCategory;
    private String image;
    private BigDecimal sellprice;


}
