package com.group3.inventhor.controller;

import com.group3.inventhor.dto.CustomerOrderProductDTO;
import com.group3.inventhor.service.CustomerOrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden.
 */

@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/customer-order-products")
@RequiredArgsConstructor
public class CustomerOrderProductController {
    private final CustomerOrderProductService service;

    /**
     * Get all Order Products (Ordernr, Productnr, Quantity).
     */
    @GetMapping
    public ResponseEntity<List<CustomerOrderProductDTO>> getAllOrderProducts() {
        return ResponseEntity.ok(service.getAllOrderProducts());
    }

    /**
     * Get all products in one order
     */
    @GetMapping("/order/{ordernr}")
    public ResponseEntity<List<CustomerOrderProductDTO>> getProductsByOrder(@PathVariable Integer ordernr) {
        return ResponseEntity.ok(service.getProductsByOrder(ordernr));
    }

    /**
     * Get a specific product in a specific order.
     */
    @GetMapping("/{ordernr}/{productnr}")
    public ResponseEntity<CustomerOrderProductDTO> getOrderProduct(
            @PathVariable Integer ordernr,
            @PathVariable Integer productnr) {
        return ResponseEntity.ok(service.getOrderProduct(ordernr, productnr));
    }

    /**
     * Add product to order
     */
    @PostMapping
    public ResponseEntity<CustomerOrderProductDTO> createOrderProduct(@RequestBody CustomerOrderProductDTO dto) {
        return ResponseEntity.ok(service.createOrderProduct(dto));
    }

    /**
     * Update order products
     */
    @PutMapping("/{ordernr}/{productnr}")
    public ResponseEntity<CustomerOrderProductDTO> updateOrderProduct(
            @PathVariable Integer ordernr,
            @PathVariable Integer productnr,
            @RequestBody CustomerOrderProductDTO dto) {
        return ResponseEntity.ok(service.updateOrderProduct(ordernr, productnr, dto));
    }

    /**
     * Delete order products
     */
    @DeleteMapping("/{ordernr}/{productnr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrderProduct(
            @PathVariable Integer ordernr,
            @PathVariable Integer productnr) {
        service.deleteOrderProduct(ordernr, productnr);
        return ResponseEntity.noContent().build();
    }
}
