package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.LocationDTO;
import com.group3.inventhor.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author Furo Muktar Eshetu
 *
 * The LocationMapper interface is used to convert between Location entities and LocationDTOs.
 *
 * @Mapper is a MapStruct annotation that generates the implementation of this interface at compile time.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface LocationMapper {
    @Mapping(source = "locationId.warehouse.warehousenr", target = "warehousenr")
    @Mapping(source = "locationId.racknr", target = "racknr")
    @Mapping(source = "locationId.placenr", target = "placenr")
    LocationDTO toLocationDTO(Location location);
    Location toLocation(LocationDTO locationDTO);
    List<LocationDTO> toLocationDTOs(List<Location> locations);
}
