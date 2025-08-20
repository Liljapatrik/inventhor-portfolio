package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.CategoryDTO;
import com.group3.inventhor.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The CategoryMapper interface is used to map between Category and CategoryDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * This interface provides methods to convert Category entities to CategoryDTOs and vice versa,
 * as well as methods to convert lists of Category entities to lists of CategoryDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *      The componentModel = "spring" allows it to be used as a Spring bean.
 *      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    CategoryDTO toCategoryDTO(Category category);
    Category toCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> toCategoryDTOs(List<Category> categories);

}
