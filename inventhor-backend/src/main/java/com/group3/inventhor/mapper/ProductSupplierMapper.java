package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.ProductSupplierDTO;
import com.group3.inventhor.dto.ProductSupplierGetSuppliersDTO;
import com.group3.inventhor.model.Category;
import com.group3.inventhor.model.ProductSupplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;


/**
 * Mapper interface for converting between link ProductSupplier entities
 * and their corresponding DTOs: link ProductSupplierDTO and link ProductSupplierGetSuppliersDTO.
 *
 * Uses MapStruct for automatic mapping generation.
 * Null values in source objects are ignored to avoid overwriting existing values with null.
 *
 * Maps nested properties from associated code product and code supplier entities to flattened DTO fields.
 * Provides a custom mapping method for converting link Category objects to their name String.
 *
 * Author: Nils Patrik Lilja
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductSupplierMapper {

    /**
     * Maps a link ProductSupplier entity to a link ProductSupplierDTO.
     * Maps nested product and supplier properties to flat DTO fields.
     *
     * param entity the ProductSupplier entity to map
     * return the mapped ProductSupplierDTO
     */
    @Mapping(source = "supplier.suppliernr", target = "suppliernr")
    @Mapping(source = "product.productnr", target = "productnr")
    @Mapping(source = "product.name", target = "productname")
    @Mapping(source = "product.image", target = "image")
    @Mapping(source = "product.category", target = "productCategory")

    ProductSupplierDTO toDTO(ProductSupplier entity);

    /**
     * Maps a link Category to its name as a String.
     * Returns null if the category is null.
     *
     * param category the Category to map
     * return the name of the category or null
     */
    default String map(Category category) {
        return category != null ? category.getName() : null;
    }

    /**
     * Maps a link ProductSupplier entity to a link ProductSupplierGetSuppliersDTO.
     *
     * param productSupplier the ProductSupplier entity to map
     * return the mapped ProductSupplierGetSuppliersDTO
     */
    // Map to ProductSupplierGetSuppliersDTO
    @Mapping(source = "supplier.contactperson", target = "supplier.contact")
    ProductSupplierGetSuppliersDTO toGetSuppliersDTO(ProductSupplier productSupplier);
}
