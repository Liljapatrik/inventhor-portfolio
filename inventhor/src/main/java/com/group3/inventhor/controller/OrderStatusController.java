package com.group3.inventhor.controller;


import com.group3.inventhor.dto.OrderStatusDTO;
import com.group3.inventhor.service.OrderStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 * Controller for managing order statuses.
 * This controller provides an endpoint to retrieve all order statuses.
 *
 * @CrossOrigin allows cross-origin requests from specified origins, which is useful for development with a frontend application running on a different port.
 * @RestController indicates that this class is a REST controller, capable of handling HTTP requests.
 * @RequestMapping specifies the base URL for all endpoints in this controller.
 * @Tag provides metadata for the controller, which is used in API documentation (e.g., Swagger).
 * @RequiredArgsConstructor generates a constructor with required arguments, allowing for dependency injection of the OrderStatusService.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"} )
@RestController
@RequestMapping("/order-status")
@Tag(name = "Order Status Controller", description = "API for managing order statuses")
@RequiredArgsConstructor
public class OrderStatusController {

    // The OrderStatusService instance used to handle order status-related operations
    private final OrderStatusService orderStatusService;

    /**
     * Get all order statuses.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve all order statuses.
     *
     * @return ResponseEntity containing a list of OrderStatusDTO with all order statuses
     */
    @Operation(summary = "Get all order statuses", description = "Retrieve a list of all order statuses")
    @GetMapping
    public ResponseEntity<List<OrderStatusDTO>> getAllOrderStatuses() {
        return ResponseEntity.ok(orderStatusService.getAllOrderStatuses());
    }


}
