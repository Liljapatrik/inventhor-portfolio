package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.InventoryWarehouseDTO;
import com.group3.inventhor.model.InventoryWarehouse;
import org.mapstruct.Mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between link InventoryWarehouse entities
 * and link InventoryWarehouseDTOData Transfer Objects.
 *
 * Uses MapStruct for automatic mapping generation.
 * Configured to ignore null properties during mapping.
 * Also uses link AddressMapper for nested address mappings.
 *
 * Author: Nils Patrik Lilja
 */
@Mapper(
        componentModel = "spring",
        uses = AddressMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InventoryWarehouseMapper {

    @Mapping(source = "warehouse.warehousenr", target = "warehousenr")
    @Mapping(source = "product.productnr", target = "productnr")

    InventoryWarehouseDTO toDTO(InventoryWarehouse inventoryWarehouse);
}
