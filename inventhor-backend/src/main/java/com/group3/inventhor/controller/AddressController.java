package com.group3.inventhor.controller;


import com.group3.inventhor.dto.AddressDTO;
import com.group3.inventhor.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Tatiana Fløisbonn
 *
 * The AddressController class is a REST controller that handles HTTP requests related to addresses in the Inventhor application.
 * It provides endpoints to get, update, and create addresses.
 *
 * @CrossOrigin allows cross-origin requests from specified origins.
 * @RestController indicates that this class is a REST controller.
 * @RequestMapping specifies the base path for all requests handled by this controller.
 * @Tag provides metadata (header and description) for Swagger documentation.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/address")
@Tag(name="Address Controller", description = "API for managing addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * Get address by addressnr.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve an address by its unique identifier (addressnr).
     *
     * @param addressnr the unique identifier for the address
     * @return ResponseEntity containing AddressDTO with address details
     *
     * @RequestParam indicates that the method expects a request parameter containing the address number.
     */
    @Operation(summary = "Get address by addressnr", description = "Retrieve address by addressnr")
    @GetMapping("/{addressnr}")
    public ResponseEntity<AddressDTO> getAddressByAddressNr(@RequestParam Integer addressnr) {
        return ResponseEntity.ok(addressService.getAddressByAddressNr(addressnr));
    }

    /**
     * Update address by addressnr.
     * @param addressnr the unique identifier for the address to be updated
     * @param addressDTO the AddressDTO containing updated address details
     * @return ResponseEntity containing updated AddressDTO
     *
     * @PathVariable indicates that the method expects a path variable containing the customer number.
     * @RequestesBody indicates that the method expects a request body containing the customer data in JSON format.
     */
    @Operation(summary = "Update address by addressnr", description = "Update information about address by addressnr")
    @PutMapping("/{addressnr}")
    public ResponseEntity<AddressDTO> updateAddressByAddressNr(@PathVariable Integer addressnr, @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.updateAddressByAddressId(addressnr, addressDTO));
    }

    /**
     * Create a new address.
     *
     * @PostMapping handles HTTP POST requests to create a new address.
     *
     * @param addressDTO the AddressDTO containing details of the new address
     * @return ResponseEntity containing created AddressDTO
     */
    @Operation(summary = "Create address", description = "Create new address")
    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.createAddress(addressDTO));
    }

    /**
     * Delete address by addressID.
     *
     * @DeleteMapping handles HTTP DELETE requests to delete an address by its unique identifier (addressnr).
     *
     * @param addressnr the unique identifier for the address to be deleted
     * @return ResponseEntity with no content status
     */
    @Operation(summary = "Delete address by addressID", description = "Delete address by addressID")
    @DeleteMapping("/{addressnr}")
    public ResponseEntity<Void> deleteAddressByAddressNr(@PathVariable Integer addressnr) {
        addressService.deleteAddressByAddressNr(addressnr);
        return ResponseEntity.noContent().build();
    }


}
