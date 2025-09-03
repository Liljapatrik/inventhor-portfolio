package com.group3.inventhor.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * @author Tatiana Fløisbonn
 *
 * This class is used to represent the price history for a current year and a past year.
 * It contains the year and a map of months with their corresponding avagage prices.
 *
 * @Data annotation generates getters, setters, toString, equals, and hashCode methods.
 * @NoArgsConstructor annotation generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class PriceHistoryYearFilterDTO {
    private Integer currentYear;
    private Map<String, Double> currentYearMonths;
    private Integer pastYear;
    private Map<String, Double> pastYearMonths;
}
