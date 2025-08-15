package com.group3.inventhor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * Contains all necessary data for the reports.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    // Sales statistics
    private BigDecimal totalSalesCurrentYear;
    private BigDecimal totalSalesPreviousYear;

    // Customer statistics
    private Integer totalCustomersCurrentYear;
    private Integer totalCustomersPreviousYear;

    // Popular products
    private Map<String, Object> weeklyPopularProduct;
    private Map<String, Object> monthlyPopularProduct;
    private Map<String, Object> yearlyPopularProduct;

    // Chart data
    private List<Map<String, Object>> monthlySalesData;
    private List<Map<String, Object>> monthlyCustomerData;
}
