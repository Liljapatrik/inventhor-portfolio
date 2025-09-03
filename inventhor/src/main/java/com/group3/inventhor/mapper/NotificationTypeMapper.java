package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.NotificationTypeDTO;
import com.group3.inventhor.model.NotificationType;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationsTypeMapper interface is used to map between NotificationType and NotificationTypeDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * This interface provides methods to convert NotificationType entities to NotificationTypeDTOs and vice versa,
 * as well as methods to convert lists of NotificationType entities to lists of NotificationTypeDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *      The componentModel = "spring" allows it to be used as a Spring bean.
 *      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationTypeMapper {

    NotificationTypeDTO toNotificationTypeDTO(NotificationType notificationType);
    NotificationType toNotificationType(NotificationTypeDTO notificationTypeDTO);
    List<NotificationTypeDTO> toNotificationTypeDTOs(List<NotificationType> notificationTypes);
}
