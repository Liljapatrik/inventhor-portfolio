package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Tatiana Fløisbonn
 *
 * The AuthUserDTO class represents a Data Transfer Object (DTO) for user authentication details.
 * It contains fields that are essential for user identification and authentication
 * This DTO is used to transfer user data between the application layers, particularly for authentication purposes.
 *
 * @Data is a Lombok annotation that generates getters, setters, equals, hashCode, and toString methods.
 * @NoArgsConstructor is a Lombok annotation that generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class AuthUserDTO {

    private Integer employeenr;
    private String email;
    private String phone;
    private String firstname;
    private String lastname;
    private String position;
    private EmployeeRoleDTO role;
    private LocalDate employeddate;
    private boolean isactive;
    private String image; // Default image URL https://img.icons8.com/?size=100&id=104948&format=png&color=000000
    private AddressDTO address;
}
