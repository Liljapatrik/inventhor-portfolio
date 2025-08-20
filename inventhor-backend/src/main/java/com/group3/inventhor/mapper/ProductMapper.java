package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.ProductDTO;
import com.group3.inventhor.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The ProductMapper interface is used to map between Product and ProductDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * In this case it converts Product entities to ProductDTOs and vice versa.
 * It also provides methods to convert lists of Product entities to lists of ProductDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *      The componentModel = "spring" allows it to be used as a Spring bean.
 *      nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    ProductDTO toProductDTO(Product product);
    Product toProduct(ProductDTO productDTO);
    List<ProductDTO> toProductDTOs(List<Product> products);
}
