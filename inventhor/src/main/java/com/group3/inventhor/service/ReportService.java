package com.group3.inventhor.service;

import com.group3.inventhor.dto.ReportDTO;
import com.group3.inventhor.model.CustomerOrder;
import com.group3.inventhor.model.CustomerOrderProduct;
import com.group3.inventhor.model.Product;
import com.group3.inventhor.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The ReportService class provides methods to get sales and customer reports for the Inventhor application.
 * Monthly and yearly data, aswell most popular product weekly, monthly and yearly.
 */

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CustomerOrderRepository customerOrderRepository;
    private static final int STATUS_CANCELLED = 5;

    /**
     * Main method for report/dashboard.
     */
    public ReportDTO getReport() {
        ReportDTO report = new ReportDTO();
        report.setTotalSalesCurrentYear(getTotalSalesCurrentYear());
        report.setTotalSalesPreviousYear(getTotalSalesPreviousYear());
        report.setTotalCustomersCurrentYear(getTotalCustomersCurrentYear());
        report.setTotalCustomersPreviousYear(getTotalCustomersPreviousYear());
        report.setWeeklyPopularProduct(getWeeklyPopularProduct());
        report.setMonthlyPopularProduct(getMonthlyPopularProduct());
        report.setYearlyPopularProduct(getYearlyPopularProduct());
        report.setMonthlySalesData(getMonthlySalesData());
        report.setMonthlyCustomerData(getMonthlyCustomerData());
        return report;
    }

    /**
     * Get total sales current year, previous year and period
     */
    public BigDecimal getTotalSalesCurrentYear() {
        return getTotalSalesForPeriod(getStartOfYear(), getEndOfYear());
    }

    public BigDecimal getTotalSalesPreviousYear() {
        int prevYear = Year.now().getValue() - 1;
        LocalDateTime start = LocalDateTime.of(prevYear, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(prevYear, 12, 31, 23, 59, 59);
        return getTotalSalesForPeriod(start, end);
    }

    private BigDecimal getTotalSalesForPeriod(LocalDateTime start, LocalDateTime end) {
        List<CustomerOrder> orders = customerOrderRepository.findByOrderdateBetween(start, end);
        BigDecimal sum = BigDecimal.ZERO;
        for (CustomerOrder order : orders) {
            if (order.getStatus().getStatusnr() != STATUS_CANCELLED) {
                for (CustomerOrderProduct op : order.getCustomerOrderProducts()) {
                    Product product = op.getProduct();
                    if (product != null) {
                        sum = sum.add(product.getSellprice().multiply(BigDecimal.valueOf(op.getQuantity())));
                    }
                }
            }
        }
        return sum;
    }

    /**
     * Get total customers current year, previous year and period
     */
    public Integer getTotalCustomersCurrentYear() {
        return getTotalCustomersForPeriod(getStartOfYear(), getEndOfYear());
    }

    public Integer getTotalCustomersPreviousYear() {
        int prevYear = Year.now().getValue() - 1;
        LocalDateTime start = LocalDateTime.of(prevYear, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(prevYear, 12, 31, 23, 59, 59);
        return getTotalCustomersForPeriod(start, end);
    }

    private Integer getTotalCustomersForPeriod(LocalDateTime start, LocalDateTime end) {
        Set<Integer> customers = new HashSet<>();
        List<CustomerOrder> orders = customerOrderRepository.findByOrderdateBetween(start, end);
        for (CustomerOrder order : orders) {
            customers.add(order.getCustomer().getCustomernr());
        }
        return customers.size();
    }

    /**
     * Get most popular products weekly, monthly and yearly
     */
    public Map<String, Object> getWeeklyPopularProduct() {
        return getPopularProductForPeriod(getStartOfWeek(), getEndOfWeek());
    }

    public Map<String, Object> getMonthlyPopularProduct() {
        return getPopularProductForPeriod(getStartOfMonth(), getEndOfMonth());
    }

    public Map<String, Object> getYearlyPopularProduct() {
        return getPopularProductForPeriod(getStartOfYear(), getEndOfYear());
    }

    public Map<String, Object> getPopularProductForPeriod(LocalDateTime start, LocalDateTime end) {
        List<CustomerOrder> orders = customerOrderRepository.findByOrderdateBetween(start, end);
        Map<Integer, Integer> productQuantity = new HashMap<>();
        Map<Integer, Product> productMap = new HashMap<>();
        for (CustomerOrder order : orders) {
            if (order.getStatus().getStatusnr() != STATUS_CANCELLED) {
                for (CustomerOrderProduct op : order.getCustomerOrderProducts()) {
                    if (op.getProduct() != null) {
                        int productId = op.getProduct().getProductnr();
                        productQuantity.put(productId, productQuantity.getOrDefault(productId, 0) + op.getQuantity());
                        productMap.put(productId, op.getProduct());
                    }
                }
            }
        }
        Optional<Map.Entry<Integer, Integer>> maxEntry = productQuantity.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        if (maxEntry.isPresent()) {
            Product p = productMap.get(maxEntry.get().getKey());
            Map<String, Object> productData = new HashMap<>();
            productData.put("productnr", p.getProductnr());
            productData.put("productName", p.getName());
            productData.put("productImage", p.getImage());
            productData.put("totalQuantitySold", maxEntry.get().getValue());
            productData.put("productDescription", p.getDescription());
            productData.put("categoryName", p.getCategory() != null ? p.getCategory().getName() : "Unknown");
            return productData;
        }
        return null;
    }

    /**
     * Get monthly sales and customer data for charts
     */

    public List<Map<String, Object>> getMonthlySalesData() {
        List<Map<String, Object>> result = new ArrayList<>();
        int currentYear = Year.now().getValue();
        int prevYear = currentYear - 1;
        result.addAll(getMonthlySales(currentYear));
        result.addAll(getMonthlySales(prevYear));
        return result;
    }

    public List<Map<String, Object>> getMonthlyCustomerData() {
        List<Map<String, Object>> result = new ArrayList<>();
        int currentYear = Year.now().getValue();
        int prevYear = currentYear - 1;
        result.addAll(getMonthlyCustomers(currentYear));
        result.addAll(getMonthlyCustomers(prevYear));
        return result;
    }

    public List<Map<String, Object>> getMonthlySales(int year) {
        List<Map<String, Object>> monthly = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime end = start.withDayOfMonth(YearMonth.of(year, month).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            BigDecimal total = getTotalSalesForPeriod(start, end);
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            Map<String, Object> data = new HashMap<>();
            data.put("month", monthName);
            data.put("monthNumber", month);
            data.put("year", year);
            data.put("totalSales", total);
            monthly.add(data);
        }
        return monthly;
    }

    public List<Map<String, Object>> getMonthlyCustomers(int year) {
        List<Map<String, Object>> monthly = new ArrayList<>();
        Set<Integer> cumulative = new HashSet<>();
        for (int month = 1; month <= 12; month++) {
            LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime end = start.withDayOfMonth(YearMonth.of(year, month).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            Set<Integer> monthCustomers = new HashSet<>();
            List<CustomerOrder> orders = customerOrderRepository.findByOrderdateBetween(start, end);
            for (CustomerOrder order : orders) {
                monthCustomers.add(order.getCustomer().getCustomernr());
            }
            Set<Integer> newCustomers = new HashSet<>(monthCustomers);
            newCustomers.removeAll(cumulative);
            cumulative.addAll(monthCustomers);
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            Map<String, Object> data = new HashMap<>();
            data.put("month", monthName);
            data.put("monthNumber", month);
            data.put("year", year);
            data.put("newCustomers", newCustomers.size());
            data.put("totalCustomers", cumulative.size());
            monthly.add(data);
        }
        return monthly;
    }

    /**
     * Helper methods for dates
     */

    private LocalDateTime getStartOfYear() {
        return LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
    private LocalDateTime getEndOfYear() {
        return LocalDateTime.now().withMonth(12).withDayOfMonth(31).withHour(23).withMinute(59).withSecond(59).withNano(0);
    }
    private LocalDateTime getStartOfMonth() {
        return LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
    private LocalDateTime getEndOfMonth() {
        return LocalDateTime.now().withDayOfMonth(YearMonth.now().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(0);
    }
    private LocalDateTime getStartOfWeek() {
        return LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
    }
    private LocalDateTime getEndOfWeek() {
        return LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
    }
}
