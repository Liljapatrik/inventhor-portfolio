package com.group3.inventhor.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The SellingHistoryDTO class represents a Data Transfer Object for sales history.
 */

@Data
public class SellingHistoryDTO {
    private Integer sellinghistorynr;
    private Integer productnr;
    private Integer quantity;
    private LocalDateTime saledate;
}
