package com.group3.inventhor.controller;

import com.group3.inventhor.dto.ReportDTO;
import com.group3.inventhor.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author Steewen Dennis Chanavi Holden.
 * Get methods for sales, customers and products.
 */

@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/reports")
@Tag(name="Report Controller", description = "API for generating reports and analytics")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Get all report data including sales, customers, and popular products
     */
    @Operation(summary = "Get report", description = "Retrieve all report data including sales, customers, and popular products")
    @GetMapping
    public ResponseEntity<ReportDTO> getReport() {
        return ResponseEntity.ok(reportService.getReport());
    }

    /**
     * Get total sales for the current year
     */
    @Operation(summary = "Get total sales this year", description = "Retrieve total sales amount for the current year")
    @GetMapping("/sales/currentyear")
    public ResponseEntity<BigDecimal> getTotalSalesCurrentYear() {
        return ResponseEntity.ok(reportService.getTotalSalesCurrentYear());
    }

    /**
     * Get total sales for the previous year
     */
    @Operation(summary = "Get total sales previous year", description = "Retrieve total sales amount for the previous year")
    @GetMapping("/sales/previousyear")
    public ResponseEntity<BigDecimal> getTotalSalesPreviousYear() {
        return ResponseEntity.ok(reportService.getTotalSalesPreviousYear());
    }

    /**
     * Get total customers for the current year
     */
    @Operation(summary = "Get total customers this year", description = "Retrieve total number of customers for the current year")
    @GetMapping("/customers/currentyear")
    public ResponseEntity<Integer> getTotalCustomersCurrentYear() {
        return ResponseEntity.ok(reportService.getTotalCustomersCurrentYear());
    }

    /**
     * Get total customers for the previous year
     */
    @Operation(summary = "Get total customers previous year", description = "Retrieve total number of customers for the previous year")
    @GetMapping("/customers/previousyear")
    public ResponseEntity<Integer> getTotalCustomersPreviousYear() {
        return ResponseEntity.ok(reportService.getTotalCustomersPreviousYear());
    }

    /**
     * Get the most popular product for the week
     */
    @Operation(summary = "Get weekly popular product", description = "Retrieve the most sold product for the current week")
    @GetMapping("/products/week")
    public ResponseEntity<Map<String, Object>> getWeeklyPopularProduct() {
        return ResponseEntity.ok(reportService.getWeeklyPopularProduct());
    }

    /**
     * Get the most popular product for the month
     */
    @Operation(summary = "Get monthly popular product", description = "Retrieve the most sold product for the current month")
    @GetMapping("/products/month")
    public ResponseEntity<Map<String, Object>> getMonthlyPopularProduct() {
        return ResponseEntity.ok(reportService.getMonthlyPopularProduct());
    }

    /**
     * Get the most popular product for the year
     */
    @Operation(summary = "Get yearly popular product", description = "Retrieve the most sold product for the current year")
    @GetMapping("/products/year")
    public ResponseEntity<Map<String, Object>> getYearlyPopularProduct() {
        return ResponseEntity.ok(reportService.getYearlyPopularProduct());
    }

    /**
     * Get monthly sales data
     */
    @Operation(summary = "Get monthly sales data", description = "Retrieve monthly sales data for charts (current and previous year)")
    @GetMapping("/sales/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlySales(@RequestParam(required = false) Integer year) {
        if (year != null) {
            return ResponseEntity.ok(reportService.getMonthlySales(year));
        }
        return ResponseEntity.ok(reportService.getMonthlySalesData());
    }

    /**
     * Get monthly customer data
     */
    @Operation(summary = "Get monthly customer data", description = "Retrieve monthly customer data for charts (current and previous year)")
    @GetMapping("/customers/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyCustomers(@RequestParam(required = false) Integer year) {
        if (year != null) {
            return ResponseEntity.ok(reportService.getMonthlyCustomers(year));
        }
        return ResponseEntity.ok(reportService.getMonthlyCustomerData());
    }
}

