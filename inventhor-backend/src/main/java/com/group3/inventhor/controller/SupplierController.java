package com.group3.inventhor.controller;

import com.group3.inventhor.dto.SupplierDTO;
import com.group3.inventhor.service.SupplierService;
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
@RequestMapping("/suppliers")
@Tag(name="Supplier Controller", description = "API for managing suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    /** GET: Supplier based on nr
     * GETMapping to handle HTTP get-request by ID to "/suppliers/{suppliernr}".
     * @PathVariable extracts the "suppliernr" value from the URL path, for example "/suppliers/1".
     * Get the supplier nr by the method getSupplierById in service-class.
     * If SupplierDTO (suppliernr) is not null, return HTTP-status OK.
     * If it is null, it returns HTTP-status NOT FOUND.
     */
    @GetMapping("/{suppliernr}")
    public ResponseEntity<SupplierDTO> getSupplier(
            @PathVariable Integer suppliernr) {

        SupplierDTO supplierDTO = supplierService.getSupplierById(suppliernr);
        if (supplierDTO != null) {
            return new ResponseEntity<>(supplierDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET: All suppliers
    * @GetMapping to handle HTTP get-request for all suppliers to /suppliers
    * Using List to get all suppliers
    * If suppliers is empty return HTTP-status NO_CONTENT
    * Else list with suppliers and return HTTP-status OK.
    */
    @GetMapping("")
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<SupplierDTO> supplierDTOS = supplierService.findAllDTOs();
        if (supplierDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(supplierDTOS, HttpStatus.OK);
    }

    /**POST: Add new supplier
    /*
    * @PostMapping to handle HTTP create-request to "/suppliers".
    * @RequestBody binds the JSON body of the request to a SupplierDTO object.
    * Checks if all fields are filled in. It cannot be null.
    * otherwise an HTTP-status BAD REQUEST will be returned.
    * Checks if email already exists by method "emailExists" in supplierService.
    * If the email exists HTTP-status CONFLICT returns.
    * If there are no BAD REQUESTS and CONFLICT the supplier till be saved with
    *  an HTTP-status CREATED.
    */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createSupplier(
            @RequestBody SupplierDTO supplierDTO,
            @RequestParam Integer employeenr) {

        if (supplierDTO.getName() == null || supplierDTO.getName().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (supplierDTO.getContact() == null || supplierDTO.getContact().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (supplierDTO.getPhone() == null || supplierDTO.getPhone().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (supplierDTO.getEmail() == null || supplierDTO.getEmail().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (supplierDTO.getWebsite() == null || supplierDTO.getWebsite().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


        if (supplierService.emailExists(supplierDTO.getEmail())) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        if (supplierService.phoneNumberExists(supplierDTO.getPhone())) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        if (supplierService.websiteExists(supplierDTO.getWebsite())) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        try {
            SupplierDTO createdSupplier = supplierService.save(supplierDTO, employeenr);
            return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);

        } catch (SecurityException securityException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }
    }

    /**
     * UPDATE supplier details
     * Handles HTTP PUT request to update a supplier's information.
     * Only accessible by users with ADMIN role.
     * param suppliernr   the ID of the supplier to update (path variable)
     * param supplierDTO  the updated supplier data (request body)
     * param employeenr   the employee number performing the update (request parameter)
     * @return ResponseEntity with updated SupplierDTO and HTTP 200 OK on success,
     * or appropriate HTTP error status on failure
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{suppliernr}")
    public ResponseEntity<?> updateSupplier(
            @PathVariable Integer suppliernr,
            @RequestBody SupplierDTO supplierDTO,
            @RequestParam Integer employeenr) {

        try {
            SupplierDTO updatedSupplier = supplierService.updateSupplier(suppliernr, supplierDTO, employeenr)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));
            return ResponseEntity.ok(updatedSupplier);

        } catch (SecurityException securityException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    /**
     * DELETE supplier by ID
     * Handles HTTP DELETE request to remove a supplier by its ID.
     * Only accessible by users with ADMIN role.
     * param suppliernr the ID of the supplier to delete (path variable)
     * param employeenr the employee number performing the deletion (request parameter)
     * return ResponseEntity with deleted SupplierDTO and HTTP 200 OK on success,
     * or appropriate HTTP error status on failure
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{suppliernr}")
    public ResponseEntity<?> deleteSupplier(
            @PathVariable Integer suppliernr,
            @RequestParam Integer employeenr) {
        try {
            SupplierDTO deletedSupplier = supplierService.deleteSupplier(suppliernr, employeenr)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));
            return ResponseEntity.ok(deletedSupplier);

        } catch (SecurityException securityException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());

        } catch (IllegalArgumentException | IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

}



