package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.OrderStatusDTO;
import com.group3.inventhor.model.OrderStatus;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * @author Tatiana Fløisbonn
 *
 * The OrderStatusMapper interface is used to map between OrderStatus and OrderStatusDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * This interface provides methods to convert OrderStatus entities to OrderStatusDTOs and vice versa,
 * as well as methods to convert lists of OrderStatus entities to lists of OrderStatusDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *      The componentModel = "spring" allows it to be used as a Spring bean.
 *      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface OrderStatusMapper {
    List<OrderStatusDTO> toOrderStatusDTOs(List<OrderStatus> orderStatuses);
}
