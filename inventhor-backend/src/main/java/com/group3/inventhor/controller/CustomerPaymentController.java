package com.group3.inventhor.controller;

import com.group3.inventhor.dto.CustomerPaymentDTO;
import com.group3.inventhor.service.CustomerPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerPaymentController class handles HTTP requests related to customer payments.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/payments")
@Tag(name="Customer Payment Controller", description = "API for managing customer payments")
@RequiredArgsConstructor
public class CustomerPaymentController {

    private final CustomerPaymentService customerPaymentService;

    /**
     * Get all payments.
     */
    @Operation(summary = "Get all payments", description = "Retrieve a list of all customer payments")
    @GetMapping
    public ResponseEntity<List<CustomerPaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(customerPaymentService.getAllPayments());
    }

    /**
     * Get payment by ID.
     */
    @Operation(summary = "Get payment by ID", description = "Retrieve a specific payment by payment number")
    @GetMapping("/{paymentnr}")
    public ResponseEntity<CustomerPaymentDTO> getPaymentById(@PathVariable Integer paymentnr) {
        return ResponseEntity.ok(customerPaymentService.getPaymentById(paymentnr));
    }

    /**
     * Get payments by order.
     */
    @Operation(summary = "Get payments by order", description = "Retrieve all payments for a specific order")
    @GetMapping("/order/{ordernr}")
    public ResponseEntity<List<CustomerPaymentDTO>> getPaymentsByOrder(@PathVariable Integer ordernr) {
        return ResponseEntity.ok(customerPaymentService.getPaymentsByOrder(ordernr));
    }

    /**
     * Get unpaid payments.
     */
    @Operation(summary = "Get unpaid payments", description = "Retrieve all payments that haven't been paid yet")
    @GetMapping("/unpaid")
    public ResponseEntity<List<CustomerPaymentDTO>> getUnpaidPayments() {
        return ResponseEntity.ok(customerPaymentService.getUnpaidPayments());
    }

    /**
     * Update Payment
     */
    @Operation(summary = "Update Payment Status", description = "Update payment status")
    @PutMapping("/{paymentnr}")
    public ResponseEntity<CustomerPaymentDTO> updatePayment(@PathVariable Integer paymentnr, @RequestBody CustomerPaymentDTO paymentDTO) {
        return ResponseEntity.ok(customerPaymentService.updatePayment(paymentnr, paymentDTO));
    }
    /**
     * Delete Payment
     */
    @Operation(summary = "Delete payment", description = "Delete a customer payment by its ID")
    @DeleteMapping("/{paymentnr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer paymentnr) {
        customerPaymentService.deletePayment(paymentnr);
        return ResponseEntity.noContent().build();
    }

}