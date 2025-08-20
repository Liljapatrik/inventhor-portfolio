package com.group3.inventhor.model;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tatiana Fløisbonn
 *
 * The Notification class represents a notification in the Inventhor application.
 * It contains details about the notification such as its type, title, message, date, read status, and the employee it is associated with.
 *
 * @Entity annotation indicates that this class is a JPA entity, meaning it represents a table in the database.
 * @Data annotation from Lombok generates getter and setter methods for all fields in the class.
 * @Table annotation specifies the name of the table in the database that this class is associated with,
 * and the schema in which the table resides, in this case, "inventhor".
 */
@Entity
@Data
@Table(name = "notification", schema = "inventhor")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationnr;

    @ManyToOne
    @JoinColumn(name = "notificationtypenr")
    private NotificationType notificationType;

    private String title;
    private String message;
    private LocalDateTime date;
    private boolean isread;

    @ManyToOne
    @JoinColumn(name = "employeenr")
    private Employee employee;
}
