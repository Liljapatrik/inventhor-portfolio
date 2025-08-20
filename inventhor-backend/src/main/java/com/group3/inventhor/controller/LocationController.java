package com.group3.inventhor.controller;


import com.group3.inventhor.dto.LocationDTO;
import com.group3.inventhor.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Furo Muktar Eshetu
 *
 * The LocationController class provides RESTful endpoints for managing locations in the Inventhor application.
 * It allows clients to perform CRUD operations on locations, such as retrieving all locations, getting a location by its number, creating a new location, updating an existing location, and deleting a location.
 *
 * @CrossOrigin annotation allows cross-origin requests from specified origins, which is useful for development with a frontend running on a different port.
 * @RestController annotation indicates that this class is a REST controller, handling HTTP requests and responses.
 * @RequestMapping annotation specifies the base URL for all endpoints in this controller.
 * @Tag annotation is used to group and document the controller in Swagger UI.
 * @RequiredArgsConstructor annotation is a Lombok annotation that generates a constructor with required arguments (final fields), allowing for dependency injection of the LocationService.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"} )
@RestController
@RequestMapping("/locations")
@Tag(name= "Location Controller", description = "API for managing locations")
@RequiredArgsConstructor
public class LocationController {

    // The LocationService instance used to handle location-related operations
    private final LocationService locationService;

    /**
     * Get all locations.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve all locations.
     *
     * @return ResponseEntity containing a list of LocationDTO with details of all locations.
     */
    @Operation(summary = "Get all locations", description = "Retrieve a list of all locations")
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    /**
     * Create a new location.
     *
     * @PostMapping handles HTTP POST requests to create a new location.
     *
     * @param locationDTO the location data to create
     * @return ResponseEntity containing the created LocationDTO
     */
    @Operation(summary = "Create a new location", description = "Create a new location")
    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        return ResponseEntity.ok(locationService.createLocation(locationDTO));
    }

}
