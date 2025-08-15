package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.LocationProductDTO;
import com.group3.inventhor.dto.LocationProductForDetailsDTO;
import com.group3.inventhor.model.LocationProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


/**
 * @author Tatiana Fløisbonn
 *
 * LocationProductMapper er en MapStruct mapper som konverterer mellom LocationProduct entiteten og LocationProductDTO.
 *
 * @Mapper annotasjonen brukes for å indikere at denne interfacet er en MapStruct mapper.
 * @Mapping annotasjonene brukes for å spesifisere hvordan feltene i LocationProduct entiteten skal mappe til feltene i LocationProductDTO.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface LocationProductMapper {
    @Mapping(source = "locationProductId.locationIdForLocationProduct.warehouse.warehousenr", target = "warehousenr")
    @Mapping(source = "locationProductId.locationIdForLocationProduct.racknr", target = "racknr")
    @Mapping(source = "locationProductId.locationIdForLocationProduct.placenr", target = "placenr")
    @Mapping(source = "locationProductId.product", target = "product")
    LocationProductDTO toLocationProductDTO(LocationProduct locationProduct);

    @Mapping(target = "locationProductId.locationIdForLocationProduct.warehouse.warehousenr", source = "warehousenr")
    @Mapping(target = "locationProductId.locationIdForLocationProduct.racknr", source = "racknr")
    @Mapping(target = "locationProductId.locationIdForLocationProduct.placenr", source = "placenr")
    @Mapping(target = "locationProductId.product", source = "product")
    LocationProduct toLocationProduct(LocationProductDTO locationProductDTO);


    @Mapping(source = "locationProductId.locationIdForLocationProduct.warehouse.warehousenr", target = "warehousenr")
    @Mapping(source = "locationProductId.locationIdForLocationProduct.racknr", target = "racknr")
    @Mapping(source = "locationProductId.locationIdForLocationProduct.placenr", target = "placenr")
    @Mapping(source = "locationProductId.product", target = "product")
    List<LocationProductDTO> toLocationProductDTOs(List<LocationProduct> locationProducts);

    @Mapping(source = "locationProductId.locationIdForLocationProduct.warehouse", target = "warehouse")
    @Mapping(source = "locationProductId.locationIdForLocationProduct.racknr", target = "racknr")
    @Mapping(source = "locationProductId.locationIdForLocationProduct.placenr", target = "placenr")
    @Mapping(source = "locationProductId.product", target = "product")
    LocationProductForDetailsDTO toLocationProductForDetailsDTO(LocationProduct locationProduct);

    @Mapping(source = "locationProductId.locationIdForLocationProduct.warehouse", target = "warehouse")
    @Mapping(source = "locationProductId.locationIdForLocationProduct.racknr", target = "racknr")
    @Mapping(source = "locationProductId.locationIdForLocationProduct.placenr", target = "placenr")
    @Mapping(source = "locationProductId.product", target = "product")
    List<LocationProductForDetailsDTO> toLocationProductForDetailsDTOs(List<LocationProduct> locationProducts);
}
