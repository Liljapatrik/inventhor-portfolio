package com.group3.inventhor.service;


import com.group3.inventhor.dto.EmployeeDTO;
import com.group3.inventhor.dto.NotificationDTO;
import com.group3.inventhor.dto.NotificationTypeDTO;
import com.group3.inventhor.mapper.NotificationMapper;
import com.group3.inventhor.model.Notification;
import com.group3.inventhor.model.NotificationType;
import com.group3.inventhor.repository.EmployeeRepository;
import com.group3.inventhor.repository.NotificationRepository;
import com.group3.inventhor.repository.NotificationTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationService class is responsible for handling business logic related to notifications.
 * It provides methods to retrieve, create, update, and delete notifications.
 *
 * @Service annotation indicates that this class is a Spring service component.
 * @RequiredArgsConstructor generates a constructor with required arguments (final fields) for dependency injection.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmployeeRepository employeeRepository;
    private final NotificationTypeRepository notificationTypeRepository;

    /**
     * Get all notifications
     *
     * @return a list of NotificationDTO objects representing all notifications in the system
     */
    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notificationMapper.toNotificationDTOs(notifications);
    }

    /**
     * Get all notifications for a specific employee
     *
     * @param employeenr the ID of the employee whose notifications are to be retrieved
     * @return a list of NotificationDTO objects representing the notifications for the specified employee
     */
    public List<NotificationDTO> getNotificationsByEmployeenr(Integer employeenr) {
        if (!employeeRepository.existsById(employeenr)) {
            throw new EntityNotFoundException("Employee with nr " + employeenr + " not found.");
        }
        List<Notification> notifications = notificationRepository.findByEmployeeEmployeenr(employeenr);
        return notificationMapper.toNotificationDTOs(notifications);
    }

    /**
     * Create a new notification
     *
     * @param notificationDTO the NotificationDTO object containing the details of the notification to be created
     * @return the created NotificationDTO object
     */
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();

        // Validate employee and notification type
        NotificationTypeDTO notificationTypeDTO = notificationDTO.getNotificationType();
        if (notificationTypeDTO != null && notificationTypeDTO.getNotificationtypenr() != null) {
            NotificationType notificationType = notificationTypeRepository.findById(notificationTypeDTO.getNotificationtypenr())
                    .orElseThrow(() -> new EntityNotFoundException("Notification type with ID " + notificationTypeDTO.getNotificationtypenr() + " not found."));
            notification.setNotificationType(notificationType);
        } else {
            throw new EntityNotFoundException("Notification type must be provided.");
        }

        EmployeeDTO employeeDTO = notificationDTO.getEmployee();
        if (employeeDTO != null && employeeDTO.getEmployeenr() != null) {
            if (!employeeRepository.existsById(employeeDTO.getEmployeenr())) {
                throw new EntityNotFoundException("Employee with ID " + employeeDTO.getEmployeenr() + " not found.");
            }
            notification.setEmployee(employeeRepository.findById(employeeDTO.getEmployeenr())
                    .orElseThrow(() -> new EntityNotFoundException("Employee with ID " + employeeDTO.getEmployeenr() + " not found.")));
        } else {
            throw new EntityNotFoundException("Employee must be provided.");
        }

        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setIsread(false);
        notification.setDate(java.time.LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationDTO(savedNotification);
    }

    /**
     * Mark a notification as read
     *
     * @param notificationnr the ID of the notification to be marked as read
     * @return the updated NotificationDTO object
     */
    public NotificationDTO markNotificationAsRead(Integer notificationnr) {
        Notification notification = notificationRepository.findById(notificationnr)
                .orElseThrow(() -> new EntityNotFoundException("Notification with nr " + notificationnr + " not found."));

        notification.setIsread(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationDTO(updatedNotification);
    }

    /**
     * Delete all notifications for a specific employee
     *
     * @param employeenr the ID of the employee whose notifications are to be deleted
     */
    public void deleteNotificationsByEmployeenr(Integer employeenr) {
        if (!employeeRepository.existsById(employeenr)) {
            throw new EntityNotFoundException("Employee with ID " + employeenr + " not found.");
        }
        List<Notification> notifications = notificationRepository.findByEmployeeEmployeenr(employeenr);
        notificationRepository.deleteAll(notifications);
    }
}
