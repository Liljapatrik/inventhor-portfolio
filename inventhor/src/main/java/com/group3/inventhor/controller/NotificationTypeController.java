package com.group3.inventhor.controller;


import com.group3.inventhor.dto.NotificationTypeDTO;
import com.group3.inventhor.service.NotificationTypeService;
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
 *
 * Controller for managing notification types.
 * This controller provides an endpoint to retrieve all notification types from the service layer.
 * It uses the NotificationTypeService to fetch the data and returns it as a list of NotificationTypeDTOs.
 *
 * @CrossOrigin allows cross-origin requests from specified origins, which is useful for development with a frontend running on a different port.
 * @RestController indicates that this class is a REST controller, capable of handling HTTP requests.
 * @RequestMapping specifies the base URL for all endpoints in this controller.
 * @Tag provides metadata for the controller, which is used for generating API documentation with Swagger.
 * @RequiredArgsConstructor generates a constructor with required arguments, allowing for dependency injection of the NotificationTypeService.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"} )
@RestController
@RequestMapping("/notification-types")
@Tag(name = "Notification Type Controller", description = "API for managing notification types")
@RequiredArgsConstructor
public class NotificationTypeController {

    // The NotificationTypeService instance used to handle notification type-related operations
    private final NotificationTypeService notificationTypeService;

    /**
     * Get all notification types
     * This endpoint retrieves all notification types from the service layer and returns them as a list of NotificationTypeDTOs.
     *
     * @Operation provides metadata for the endpoint, including a summary and description for Swagger documentation.
     * @GetMapping specifies that this method handles GET requests to the "/notification-types" endpoint.
     *
     * @return ResponseEntity containing a list of NotificationTypeDTOs and an HTTP status code.
     */
    @Operation(summary = "Get all notification types", description = "Retrieves all notification types from the service layer")
    @GetMapping
    public ResponseEntity<List<NotificationTypeDTO>> getAllNotificationTypes() {
        // Call the service to get all notification types and return them as a ResponseEntity
        List<NotificationTypeDTO> notificationTypes = notificationTypeService.getAllNotificationTypes();
        return ResponseEntity.ok(notificationTypes);
    }
}
