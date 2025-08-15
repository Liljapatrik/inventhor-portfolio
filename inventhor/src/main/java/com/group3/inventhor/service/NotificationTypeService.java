package com.group3.inventhor.service;


import com.group3.inventhor.dto.NotificationTypeDTO;
import com.group3.inventhor.mapper.NotificationTypeMapper;
import com.group3.inventhor.model.NotificationType;
import com.group3.inventhor.repository.NotificationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationTypeService class is responsible for handling business logic related to notification types.
 *
 * @Service annotation indicates that this class is a Spring service component.
 * @RequiredArgsConstructor generates a constructor with required arguments (final fields) for dependency injection.
 */
@Service
@RequiredArgsConstructor
public class NotificationTypeService {

    // This service class is responsible for handling business logic related to notification types.
    private final NotificationTypeRepository notificationTypeRepository;
    //
    private final NotificationTypeMapper notificationTypeMapper;

    /**
     * Get all notification types.
     *
     * @return List of NotificationTypeDTO containing all notification types.
     */
    public List<NotificationTypeDTO> getAllNotificationTypes() {
        // Retrieve all notification types from the repository and convert them to DTOs
        List<NotificationType> notificationTypes = notificationTypeRepository.findAll();
        return notificationTypeMapper.toNotificationTypeDTOs(notificationTypes);
    }

}



