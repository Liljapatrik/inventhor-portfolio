package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.PaymentMethodDTO;
import com.group3.inventhor.model.PaymentMethod;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * @author Tatiana Fløisbonn
 *
 * The PaymentMethodMapper interface is used to map between PaymentMethod and PaymentMethodDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * This interface provides methods to convert PaymentMethod entities to PaymentMethodDTOs and vice versa,
 * as well as methods to convert lists of PaymentMethod entities to lists of PaymentMethodDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *      The componentModel = "spring" allows it to be used as a Spring bean.
 *      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMethodMapper {
    PaymentMethodDTO toPaymentMethodDTO(PaymentMethod paymentMethod);
    PaymentMethod toPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    List<PaymentMethodDTO> toPaymentMethodDTOs(List<PaymentMethod> paymentMethods);
}
