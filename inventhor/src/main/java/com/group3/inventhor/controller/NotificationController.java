package com.group3.inventhor.controller;


import com.group3.inventhor.dto.NotificationDTO;
import com.group3.inventhor.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * NotificationController is a REST controller that handles HTTP requests related to notifications.
 * It provides endpoints to create, retrieve, update, and delete notifications for employees.
 *
 * @CrossOrigin allows cross-origin requests from specified origins, enabling the frontend application to interact with this API.
 * @RestController indicates that this class is a RESTful controller, capable of handling HTTP requests.
 * @RequestMapping specifies the base URL for all endpoints in this controller.
 * @Tag provides metadata for the controller, which is used in Swagger documentation to describe the API.
 * @RequiredArgsConstructor generates a constructor with required arguments, allowing for dependency injection of the NotificationService.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"} )
@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification Controller", description = "API for managing notifications")
@RequiredArgsConstructor
public class NotificationController {

    // The NotificationService instance used to handle notification-related operations
    private final NotificationService notificationService;

    /**
     * Get all notifications.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve all notifications.
     *
     * @return ResponseEntity containing a list of NotificationDTO with details of all notifications.
     */
    @Operation(summary = "Get all notifications", description = "Retrieve a list of all notifications")
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    /**
     * Get all notifications for a specific employee.
     *
     * @param employeenr the ID of the employee whose notifications are to be retrieved
     * @return ResponseEntity containing a list of NotificationDTO with details of all notifications for the specified employee
     */
    @Operation(summary = "Get notifications by employee nr", description = "Retrieve notifications for a specific employee")
    @GetMapping("/{employeenr}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByEmployeenr(@PathVariable Integer employeenr) {
        return ResponseEntity.ok(notificationService.getNotificationsByEmployeenr(employeenr));
    }

    /**
     * Create a new notification.
     *
     * @PostMapping handles HTTP POST requests to create a new notification.
     *
     * @param notificationDTO the NotificationDTO object containing the details of the notification to be created
     * @return ResponseEntity containing the created NotificationDTO object
     */
    @Operation(summary = "Create a new notification", description = "Create a new notification")
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        return ResponseEntity.ok(notificationService.createNotification(notificationDTO));
    }

    /**
     * Mark a notification as read.
     *
     * @PutMapping handles HTTP PUT requests to mark a notification as read.
     *
     * @param notificationnr the ID of the notification to be marked as read
     * @return ResponseEntity indicating the success of the operation
     */
    @Operation(summary = "Mark notification as read", description = "Mark a notification as read")
    @PutMapping("/{notificationnr}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Integer notificationnr) {
        notificationService.markNotificationAsRead(notificationnr);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all notifications for a specific employee.
     *
     * @DeleteMapping handles HTTP DELETE requests to delete all notifications for a specific employee.
     *
     * @param employeenr the ID of the employee whose notifications are to be deleted
     */
    @Operation(summary = "Delete all notifications for employee", description = "Delete all notifications for a specific employee")
    @DeleteMapping("/{employeenr}")
    public ResponseEntity<Void> deleteNotificationsByEmployeenr(@PathVariable Integer employeenr) {
        notificationService.deleteNotificationsByEmployeenr(employeenr);
        return ResponseEntity.noContent().build();
    }
}
