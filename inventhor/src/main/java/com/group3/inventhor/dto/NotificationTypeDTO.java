package com.group3.inventhor.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationTypeDTO class represents a Data Transfer Object (DTO) for the NotificationType entity in the Inventhor application.
 * It is used to transfer notification type data between different layers of the application, such as from the service layer to the controller layer.
 *
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class, also all arguments constructor
 * @NoArgsConstructor generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class NotificationTypeDTO {
    private Integer notificationtypenr;
    private String name;
}
