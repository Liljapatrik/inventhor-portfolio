package com.group3.inventhor.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationDTO class represents a Data Transfer Object for notifications.
 * It contains fields for notification number, type, title, message, date, read status, and associated employee.
 *
 * This class is used to transfer notification data between different layers of the application, such as from the service layer to the controller layer.
 *
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class, as well as a constructor with all arguments.
 * @NoArgsConstructor generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class NotificationDTO {
    private Integer notificationnr;
    private NotificationTypeDTO notificationType;
    private String title;
    private String message;
    private LocalDateTime date;
    private boolean isread;
    private EmployeeDTO employee;
}
