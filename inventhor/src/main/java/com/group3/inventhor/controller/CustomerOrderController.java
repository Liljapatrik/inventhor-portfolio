package com.group3.inventhor.controller;

import com.group3.inventhor.dto.CustomerOrderCreateDTO;
import com.group3.inventhor.dto.CustomerOrderDTO;
import com.group3.inventhor.service.CustomerOrderService;
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
@RequestMapping("/customer-orders")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final CustomerOrderService orderService;

    /**
     * Get all Orders.
     */
    @GetMapping
    public ResponseEntity<List<CustomerOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Get specific order by order number.
     */
    @GetMapping("/{ordernr}")
    public ResponseEntity<CustomerOrderDTO> getOrderById(@PathVariable Integer ordernr) {
        return ResponseEntity.ok(orderService.getOrderById(ordernr));
    }

    /**
     * Create new order.
     */
    @PostMapping
    public ResponseEntity<CustomerOrderDTO> createOrder(@RequestBody CustomerOrderCreateDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    /**
     * Update an Order.
     */
    @PutMapping("/{ordernr}")
    public ResponseEntity<CustomerOrderDTO> updateOrder(@PathVariable Integer ordernr, @RequestBody CustomerOrderDTO dto) {
        return ResponseEntity.ok(orderService.updateOrder(ordernr, dto));
    }

    /**
     * Delete an Order.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{ordernr}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer ordernr) {
        orderService.deleteOrder(ordernr);
        return ResponseEntity.noContent().build();
    }
}

