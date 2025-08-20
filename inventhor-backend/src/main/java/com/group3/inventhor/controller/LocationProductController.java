package com.group3.inventhor.controller;


import com.group3.inventhor.dto.LocationProductDTO;
import com.group3.inventhor.dto.LocationProductForDetailsDTO;
import com.group3.inventhor.service.LocationProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author Tatiana Fløisbonn
 *
 * Controller for managing location products in the warehouse.
 * This controller provides endpoints to retrieve, create, and delete location products.
 * It allows users to get all location products, filter them by warehouse number,
 * create new location products, and delete existing ones based on their warehouse, rack, place, and product numbers.
 *
 * @CrossOrigin allows cross-origin requests from specified origins, which is useful for development with a frontend application running on a different port.
 * @RestController indicates that this class is a REST controller, handling HTTP requests and responses.
 * @RequestMapping specifies the base URL for all endpoints in this controller.
 * @Tag provides metadata for the controller, which is used in Swagger documentation to describe the API.
 * @RequiredArgsConstructor generates a constructor with required arguments, allowing for dependency injection of the LocationProductService.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/location-product")
@Tag(name="Location Product Controller", description = "API for managing location products")
@RequiredArgsConstructor
public class LocationProductController {

    private final LocationProductService locationProductService;

    /**
     * Get all location products.
     *
     * @Operation provides a description of the method for Swagger documentation.
     * @GetMapping handles the HTTP GET request.
     *
     * @return ResponseEntity containing a list of all location products.
     */
    @Operation(summary = "Get all location products", description = "Retrieve a list of all location products")
    @GetMapping
    public ResponseEntity<List<LocationProductDTO>> getAllLocationProducts() {
        return ResponseEntity.ok(locationProductService.getAllLocationProducts());
    }

    /**
     * Get locarions products by warehouse number.
     *
     * @param warehousenr The warehouse number to filter location products.
     * @return ResponseEntity containing a list of location products for the specified warehouse.
     */
    @Operation(summary = "Get location products by warehouse number", description = "Retrieve a list of location products for a specific warehouse")
    @GetMapping("/warehouse/{warehousenr}")
    public ResponseEntity<List<LocationProductDTO>> getLocationProductsByWarehouse(@PathVariable Integer warehousenr) {
        return ResponseEntity.ok(locationProductService.getLocationProductsByWarehouse(warehousenr));
    }

    /**
     * Get all locations for product by product number.
     * This method retrieves all locations where a specific product is stored.
     *
     * @param productnr The product number to filter locations.
     * @return ResponseEntity containing a list of locations for the specified product.
     */
    @Operation(summary = "Get all locations for product by product number", description = "Retrieve a list of locations where a specific product is stored")
    @GetMapping("/product/{productnr}")
    public ResponseEntity<List<LocationProductForDetailsDTO>> getAllLocationsForProduct(@PathVariable Integer productnr) {
        return ResponseEntity.ok(locationProductService.getAllLocationsForProduct(productnr));
    }

    /**
     * Create a new location product.
     *
     * @PostMapping handles the HTTP POST request.
     *
     * @param locationProductDTO The DTO object representing the location product to be added.
     * @return ResponseEntity containing the created location product.
     */
    @Operation(summary = "Create a new location product", description = "Add a new location product")
    @PostMapping()
    public ResponseEntity<LocationProductDTO> createLocationProduct(@RequestBody LocationProductDTO locationProductDTO) {
        return ResponseEntity.ok(locationProductService.createLocationProduct(locationProductDTO));
    }

    /**
     * Update a location product quantity.
     *
     * @param warehousenr the warehouse number
     * @param racknr the rack number
     * @param placenr the place number
     * @param productnr the product number
     * @param request map containing the new quantity
     * @return ResponseEntity containing the updated location product
     */
    @Operation(summary = "Update location product quantity", description = "Update the quantity of a location product")
    @PutMapping("/warehouse/{warehousenr}/rack/{racknr}/place/{placenr}/product/{productnr}")
    public ResponseEntity<LocationProductDTO> updateLocationProductQuantity(
            @PathVariable Integer warehousenr,
            @PathVariable Integer racknr,
            @PathVariable Integer placenr,
            @PathVariable Integer productnr,
            @RequestBody Map<String, BigDecimal> request) {

        BigDecimal quantity = request.get("quantity");
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }

        LocationProductDTO updatedLocationProduct = locationProductService.updateLocationProductQuantity(
                warehousenr, racknr, placenr, productnr, quantity);

        return ResponseEntity.ok(updatedLocationProduct);
    }

    /**
     * Delete a location product.
     *
     * @DeleteMapping handles the HTTP DELETE request.
     *
     * @param warehousenr The warehouse number of the location product to be deleted.
     * @param racknr The rack number of the location product to be deleted.
     * @param placenr The place number of the location product to be deleted.
     * @param productnr The product number of the location product to be deleted.
     *
     * @return ResponseEntity indicating the result of the deletion operation.
     */
    @Operation(summary = "Delete a location product", description = "Delete a location product by its warehouse, rack, place, and product numbers")
    @DeleteMapping("/warehouse/{warehousenr}/rack/{racknr}/place/{placenr}/product/{productnr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLocationProduct(@PathVariable Integer warehousenr,@PathVariable Integer racknr,@PathVariable Integer placenr,@PathVariable Integer productnr) {
        locationProductService.deleteLocationProduct(warehousenr, racknr, placenr, productnr);
        return ResponseEntity.noContent().build();
    }


}
