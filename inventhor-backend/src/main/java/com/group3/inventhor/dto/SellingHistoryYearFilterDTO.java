package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Tatiana Fløisbonn
 *
 * DTO that stores the selling history for a year, including the current year and the past year.
 * It contains the total sales for each month in both years.
 *
 * @Data generates getters, setters, toString, equals, and hashCode methods.
 * @NoArgsConstructor generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class SellingHistoryYearFilterDTO {
    private Integer currentYear;
    private Map<String, Double> currentYearMonths;
    private Integer pastYear;
    private Map<String, Double> pastYearMonths;
}
