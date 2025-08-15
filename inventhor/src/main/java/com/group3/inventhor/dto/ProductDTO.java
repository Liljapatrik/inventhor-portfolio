package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Tatiana Fløisbonn
 *
 * The AuthDTO class is a placeholder for authentication-related data transfer objects.
 * It can be used to transfer authentication data between different layers of the application.
 *
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class, also all arguments constructor.
 * @NoArgsConstructor generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class ProductDTO {
    private Integer productnr;
    private String image; // Default image URL https://img.icons8.com/?size=100&id=104948&format=png&color=000000
    private String name;
    private String description;
    private CategoryDTO category;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal depth;
    private BigDecimal weight;
    private BigDecimal sellprice;
    private String unit;
    private Integer quantity; // Summary quantity of the product across all locations in all warehouses
}
