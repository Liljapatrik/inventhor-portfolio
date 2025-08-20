package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * * @author Tatiana Fløisbonn
 *
 * The EmployeeDTO class represents a Data Transfer Object (DTO) for the Employee entity in the Inventhor application.
 *      It is used to transfer employee data between different layers of the application, such as from the service layer to the controller layer.
 *
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class, also all arguments constructor
 * @NoArgsConstructor generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class EmployeeDTO {
    private Integer employeenr;
    private String email;
    private String phone;
    private String password;
    private String firstname;
    private String lastname;
    private String position;
    private EmployeeRoleDTO role;
    private LocalDate employeddate;
    private boolean isactive;
    private String image; // Default image URL https://img.icons8.com/?size=100&id=104948&format=png&color=000000
    private AddressDTO address;

}