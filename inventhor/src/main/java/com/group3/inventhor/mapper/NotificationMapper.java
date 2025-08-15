package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.NotificationDTO;
import com.group3.inventhor.model.Notification;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationMapper interface is used to map between Notification and NotificationDTO objects.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 * The componentModel = "spring" allows it to be used as a Spring bean.
 * nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {

    NotificationDTO toNotificationDTO(Notification notification);
    Notification toNotification(NotificationDTO notificationDTO);
    List<NotificationDTO> toNotificationDTOs(List<Notification> notifications);
}
