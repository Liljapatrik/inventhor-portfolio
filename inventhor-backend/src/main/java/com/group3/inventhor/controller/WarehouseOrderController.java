package com.group3.inventhor.controller;

import com.group3.inventhor.dto.WarehouseOrderCreateDTO;
import com.group3.inventhor.dto.WarehouseOrderDTO;
import com.group3.inventhor.service.WarehouseOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author Nils Patrik Lilja
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/warehouse-orders")
@Tag(name="Warehouseorder Controller", description = "API for managing warehouseorder")
@RequiredArgsConstructor
public class WarehouseOrderController {
    private final WarehouseOrderService warehouseOrdersService;

    /**
     * GET warehouse order by ID
     * Handles HTTP GET request to retrieve a warehouse order by its order number.
     * param ordernr the ID of the warehouse order to retrieve (path variable)
     * return ResponseEntity with WarehouseOrderDTO and HTTP 200 OK if found,
     * or HTTP 404 NOT FOUND if the order does not exist.
     */
    @GetMapping("/{ordernr}")
    public ResponseEntity<WarehouseOrderDTO> getWarehouseOrder(
            @PathVariable Integer ordernr) {

        WarehouseOrderDTO warehouseOrdersDTO = warehouseOrdersService.getWarehouseOrderById(ordernr);
        if (warehouseOrdersDTO != null) {
            return new ResponseEntity<>(warehouseOrdersDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    /**
     * GET all warehouse orders
     * Handles HTTP GET request to retrieve all warehouse orders.
     * return ResponseEntity with a list of WarehouseOrderDTO and HTTP 200 OK if orders exist,
     * or HTTP 204 NO CONTENT if there are no warehouse orders.
     */
    @GetMapping("")
    public ResponseEntity<List<WarehouseOrderDTO>> getWarehouseOrders() {
        List<WarehouseOrderDTO> warehouseOrderDTOS = warehouseOrdersService.findAllDTOs();
        if (warehouseOrderDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(warehouseOrderDTOS, HttpStatus.OK);
    }

    /**
     * CREATE a new warehouse order
     * Handles HTTP POST request to create a new warehouse order.
     * Only accessible by users with ADMIN role.
     * param dto the data transfer object containing warehouse order details (request body)
     * param employeenr  the employee number performing the creation (request parameter)
     * return ResponseEntity with created WarehouseOrderDTO and HTTP 201 CREATED on success,
     * or appropriate HTTP error status on failure:
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createWarehouseOrder(
            @RequestBody WarehouseOrderCreateDTO dto,
            @RequestParam Integer employeenr) {

        try {
            WarehouseOrderDTO createdOrder = warehouseOrdersService.createWarehouseOrder(dto, employeenr);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }  catch (SecurityException securityException) {
            return new ResponseEntity<>(securityException.getMessage(), HttpStatus.FORBIDDEN);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * UPDATE warehouse order by ID
     * Handles HTTP PUT request to update an existing warehouse order.
     * Only accessible by users with ADMIN role.
     * param ordernr the ID of the warehouse order to update (path variable)
     * param dto the updated warehouse order data (request body)
     * param employeenr the employee number performing the update (request parameter)
     * @return ResponseEntity with updated WarehouseOrderDTO and HTTP 200 OK on success,
     * or appropriate HTTP error status on failure:
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{ordernr}")
    public ResponseEntity<?> updateWarehouseOrder(
            @PathVariable Integer ordernr,
            @RequestBody WarehouseOrderDTO dto,
            @RequestParam Integer employeenr) {

        try {
            WarehouseOrderDTO updatedOrder = warehouseOrdersService.updateWarehouseOrder(ordernr, dto, employeenr);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);

        } catch (SecurityException securityException) {
            return new ResponseEntity<>(securityException.getMessage(), HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * DELETE warehouse order by ID
     * Handles HTTP DELETE request to delete a warehouse order by its ID.
     * Only accessible by users with ADMIN role.
     * param ordernr the ID of the warehouse order to delete (path variable)
     * param employeenr the employee number performing the deletion (request parameter)
     * return ResponseEntity with HTTP 204 NO CONTENT on successful deletion,
     * or appropriate HTTP error status on failure:
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{ordernr}")
    public ResponseEntity<?> deleteWarehouseOrder(
            @PathVariable Integer ordernr,
            @RequestParam Integer employeenr) {
        try {
            warehouseOrdersService.deleteWarehouseOrder(ordernr, employeenr);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (SecurityException securityException) {
            return new ResponseEntity<>(securityException.getMessage(), HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
