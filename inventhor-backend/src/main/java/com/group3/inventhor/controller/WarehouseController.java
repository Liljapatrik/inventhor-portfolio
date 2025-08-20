package com.group3.inventhor.controller;

import com.group3.inventhor.dto.WarehouseDTO;
import com.group3.inventhor.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author Furo Muktar Eshetu
*/

@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/warehouses")
@Tag(name = "Warehouse Controller", description = "API for managing warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    /**
     * Get all warehouses.
     */

    @Operation(summary = "get all warehouses")
    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouses() {
        List<WarehouseDTO> list = warehouseService.findAll();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    /**
     * Get a single warehouse by ID.
     */
    @Operation(summary = "Get warehouse by ID")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouse(@PathVariable Integer id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    /**
     * Create a new warehouse.
     */
    @Operation(summary = "Create a new warehouse")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(
            @RequestBody WarehouseDTO dto,
            @RequestParam Integer employeenr) {
        return ResponseEntity.ok(warehouseService.createWarehouse(dto, employeenr));
    }

    /**
     * Update an existing warehouse.
     */
    @Operation(summary = "Update warehouse")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWarehouse(
            @PathVariable Integer id,
            @RequestBody WarehouseDTO dto,
            @RequestParam Integer employeenr) {
        return warehouseService.updateWarehouse(id, dto, employeenr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a warehouse by ID.
     */
    @Operation(summary = "Delete warehouse")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouse(
            @PathVariable Integer id,
            @RequestParam Integer employeenr) {
        return warehouseService.deleteWarehouse(id, employeenr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

