package com.group3.inventhor.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The priceHistoryDTO class represents a Data Transfer Object for sales history.
 */

@Data
public class PriceHistoryDTO {
    private Integer pricehistorynr;
    private Integer productnr;
    private BigDecimal sellprice;
    private BigDecimal buyprice;
    private LocalDateTime setdate;
}
