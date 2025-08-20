package com.group3.inventhor.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Furo Muktar Eshetu
 *
 * The LocationDTO class represents a Data Transfer Object (DTO) for the Location entity in the Inventhor application.
 * It is used to transfer location data between different layers of the application, such as from the service layer to the controller layer.
 *
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class, also all arguments constructor
 * @NoArgsConstructor generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class LocationDTO {

    private Integer warehousenr;
    private Integer racknr;
    private Integer placenr;
}
