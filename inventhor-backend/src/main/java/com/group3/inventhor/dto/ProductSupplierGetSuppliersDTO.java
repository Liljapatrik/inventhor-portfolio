package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tatiana Fløisbonn
 *
 * ProductSupplierGetSuppliersDTO is used to get all suppliers for a product.
 * It contains the product number and a SupplierDTO object.
 * This DTO is used in the ProductSupplierController to return a list of suppliers for a specific product.
 *
 * @Data is a Lombok annotation that generates getters, setters, toString, equals, and hashCode methods.
 * @NoArgsConstructor is a Lombok annotation that generates a no-argument constructor.
 */

@Data
@NoArgsConstructor
public class ProductSupplierGetSuppliersDTO {
    private Integer productnr;
    private SupplierDTO supplier;
}
