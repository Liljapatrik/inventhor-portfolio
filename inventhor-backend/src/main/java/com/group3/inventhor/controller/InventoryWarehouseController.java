package com.group3.inventhor.controller;

import com.group3.inventhor.dto.InventoryWarehouseDTO;
import com.group3.inventhor.service.InventoryWarehouseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author Nils Patrik Lilja
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/inventory-warehouse")
@Tag(name="Inventorywarehouse Controller", description = "API for managing inventorywarehouse")
@RequiredArgsConstructor
public class InventoryWarehouseController {
    private final InventoryWarehouseService inventoryWarehouseService;

    /**
     * GET InventoryWarehouse BY ID
     * PathVariable extracts the "warehousenr" and "productnr" value from the URL path, for example "/inventory-warehouse/1/1".
     * Get the ID:s by the method getProductByWarehouse in service-class.
     * If InventoryWarehouseDTO is not null, return HTTP-status OK.
     * If it is null, it returns HTTP-status NOT FOUND.
     */
    @GetMapping("/{warehousenr}/{productnr}")
    public ResponseEntity<InventoryWarehouseDTO> getInventoryWarehouse(
            @PathVariable Integer warehousenr,
            @PathVariable Integer productnr) {
        InventoryWarehouseDTO inventoryWarehouseDTO = inventoryWarehouseService.getProductByWarehouse(warehousenr, productnr);
        if (inventoryWarehouseDTO != null) {
            return new ResponseEntity<>(inventoryWarehouseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET ALL
     * GetMapping to handle HTTP get-request for all warehouse and product
     * Using List to get products for specific warehouse
     * If list is empty, returns no content, otherwise OK
     * */
    @GetMapping("{warehousenr}")
    public ResponseEntity<List<InventoryWarehouseDTO>> getInventoryWarehouses(
            @PathVariable Integer warehousenr) {
        List<InventoryWarehouseDTO> inventory = inventoryWarehouseService.getProductsByWarehouse(warehousenr);
                if (inventory.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    /**
     * POST - Create new inventory item in a warehouse
     * Only Admin hase authority to handle post
     * InventoryWarehouse - The inventory data to be created (sent in the body as JSON)
     * A query parameter representing the employee who is performing the action
     * Calls the service method to create the inventory item.
     * If successful, it returns the created item and HTTP status 201 Created.
     * The method handles various exceptions and returns appropriate HTTP status codes
     */
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createInventoryWarehouse(
            @RequestBody InventoryWarehouseDTO inventoryWarehouseDTO,
            @RequestParam Integer employeenr) {
        try {
            InventoryWarehouseDTO createdInventoryWarehouse = inventoryWarehouseService.createInventoryWarehouse(
                    inventoryWarehouseDTO, employeenr);
            return new ResponseEntity<>(createdInventoryWarehouse, HttpStatus.CREATED);
        } catch (SecurityException securityException) {
            return new ResponseEntity<>(securityException, HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Only users with the ADMIN role are allowed to perform this update.
     * warehousenr - ID of the warehouse (from the URL path).
     * productnr - ID of the product to update (also from the URL path).
     * inventoryWarehouseDTO - The new data to update the item with (sent in the request body as JSON).
     * employeenr - Employee number (sent as a query parameter) representing who is performing the update.
     * Calls a service method to update the specific product in the given warehouse.
     * If successful, returns the updated item and HTTP status 200 OK.
     * The method catches and handles several exception types
     */
    @PutMapping("/{warehousenr}/{productnr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateInventoryWarehouse(
            @PathVariable Integer warehousenr,
            @PathVariable Integer productnr,
            @RequestBody InventoryWarehouseDTO inventoryWarehouseDTO,
            @RequestParam Integer employeenr) {
        try {
            InventoryWarehouseDTO updated = inventoryWarehouseService.updateInventoryWarehouse(warehousenr, productnr, inventoryWarehouseDTO, employeenr);
            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (SecurityException securityException) {
            return new ResponseEntity<>(securityException, HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Only users with the ADMIN role are allowed to delete inventory items.
     * warehousenr: The warehouse number (from the URL path).
     * productnr: The product number to be deleted (from the URL path).
     * employeenr: The employee number (from query parameter) performing the deletion.
     * Calls the service layer to delete the product from the specified warehouse.
     * The service method returns an Optional<InventoryWarehouseDTO>, which:
     * If present, the product was found and deleted.
     * If not present, throws a ResponseStatusException with 404 Not Found.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{warehousenr}/{productnr}")
    public ResponseEntity<?> deleteInventoryWarehouse(
            @PathVariable Integer warehousenr,
            @PathVariable Integer productnr,
            @RequestParam Integer employeenr) {
        try {
            InventoryWarehouseDTO deletedInventoryWarehouse = inventoryWarehouseService.deleteInventoryWarehouse(warehousenr, productnr, employeenr)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
            return ResponseEntity.ok(deletedInventoryWarehouse);

        } catch (SecurityException securityException) {
            return new ResponseEntity<>(securityException, HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
