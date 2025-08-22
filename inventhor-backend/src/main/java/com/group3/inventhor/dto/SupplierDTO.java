package com.group3.inventhor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Nils Patrik Lilja
 */

/**
 * Data Transfer Object (DTO) representing a supplier entity.
 * Contains supplier identification, contact information, and associated address and employee details.
 * Validation annotations ensure required fields are not null.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SupplierDTO {
    private Integer suppliernr;

    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "contact is required")
    private String contact;
    @NotNull(message = "website is required")
    private String website;
    @NotNull(message = "phone number is required")
    private String phone;
    @NotNull(message = "email is required")
    private String email;
    @NotNull(message = "notes is required")
    private String notes;

    private AddressDTO address;

}
