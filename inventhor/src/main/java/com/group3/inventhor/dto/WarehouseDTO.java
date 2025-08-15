package com.group3.inventhor.dto;


import com.group3.inventhor.model.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
@author Furo Muktar Eshetu
the warehouse class represents a warehouse entity in the inventhor application
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {

    private Integer warehousenr;

    @NotNull(message = "Name is required")
    private String name;

    private AddressDTO address;
}

