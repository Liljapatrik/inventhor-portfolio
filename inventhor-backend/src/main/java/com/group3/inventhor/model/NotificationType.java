package com.group3.inventhor.model;


import jakarta.persistence.*;
import lombok.Data;


/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationType class represents a notification type entity in the Inventhor application.
 * It is used to define different types of notifications that can be sent to users.
 *
 * @Entity annotation indicates that this class is a JPA entity,
 * meaning it represents a table in the database.
 * @Data annotation from Lombok generates getter and setter methods for all fields in the class.
 * @Table annotation specifies the name of the table in the database that this class is associated with,
 * and the schema in which the table resides, in this case, "inventhor".
 */
@Entity
@Data
@Table(name = "notificationtype", schema = "inventhor")
public class NotificationType {

    // Unique identifier for the notification type, automatically generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationtypenr;

    // Name of the notification type
    private String name;
}
