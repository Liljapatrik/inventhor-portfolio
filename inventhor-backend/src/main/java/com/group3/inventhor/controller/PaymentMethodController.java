package com.group3.inventhor.controller;


import com.group3.inventhor.dto.PaymentMethodDTO;
import com.group3.inventhor.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * This controller handles HTTP requests related to payment methods.
 * It provides endpoints to retrieve, create, and manage payment methods.
 * It uses the PaymentMethodService to perform operations on payment methods.
 *
 * @CrossOrigin annotation allows cross-origin requests from specified origins.
 * @RestController annotation indicates that this class is a REST controller.
 * @RequestMapping annotation specifies the base URL for all endpoints in this controller.
 * @Tag annotation is used for Swagger documentation to describe the controller.
 * @RequiredArgsConstructor annotation generates a constructor with required arguments for dependency injection.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"} )
@RestController
@RequestMapping("/payment-methods")
@Tag(name = "Payment Method Controller", description = "API for managing payment methods")
@RequiredArgsConstructor
public class PaymentMethodController {


    private final PaymentMethodService paymentMethodService;

    /**
     * Get payment method by payment method number.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve a payment method by its unique identifier (paymentmethodnr).
     *
     * @param paymentmethodnr the unique identifier for the payment method
     * @return ResponseEntity containing PaymentMethodDTO with payment method details
     */
    @Operation(summary = "Get payment method by payment method number", description = "Retrieve payment method by payment method number")
    @GetMapping("/{paymentmethodnr}")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodByPaymentMethodnr(@RequestParam Integer paymentmethodnr) {
        return ResponseEntity.ok(paymentMethodService.getPaymentMethodByPaymentMethodnr(paymentmethodnr));
    }

    /**
     * Get all payment methods.
     *
     * @return ResponseEntity containing a list of PaymentMethodDTO with all payment methods
     */
    @Operation(summary = "Get all payment methods", description = "Retrieve a list of all payment methods")
    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethods() {
        return ResponseEntity.ok(paymentMethodService.getAllPaymentMethods());
    }

    /**
     * Create a new payment method.
     *
     * @PostMapping handles HTTP POST requests to create a new payment method.
     *
     * @param paymentMethodDTO the PaymentMethodDTO containing payment method details to be created
     * @return ResponseEntity containing the created PaymentMethodDTO
     */
    @Operation(summary = "Create a new payment method", description = "Create a new payment method")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        return ResponseEntity.ok(paymentMethodService.createPaymentMethod(paymentMethodDTO));
    }

}
