package com.group3.inventhor.service;


import com.group3.inventhor.dto.LocationProductDTO;
import com.group3.inventhor.dto.LocationProductForDetailsDTO;
import com.group3.inventhor.mapper.LocationProductMapper;
import com.group3.inventhor.model.Location;
import com.group3.inventhor.model.LocationIdForLocationProduct;
import com.group3.inventhor.model.LocationProduct;
import com.group3.inventhor.model.LocationProductId;
import com.group3.inventhor.repository.LocationProductRepository;
import com.group3.inventhor.repository.LocationRepository;
import com.group3.inventhor.repository.ProductRepository;
import com.group3.inventhor.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author Tatiana Fløisbonn
 *
 * LocationProductService is a service class that handles business logic related to location products.
 * It provides methods to retrieve, create, and delete location products.
 *
 * @Service annotation indicates that this class is a service component in the Spring framework.
 * @RequiredArgsConstructor annotation generates a constructor with required arguments for dependency injection.
 */
@Service
@RequiredArgsConstructor
public class LocationProductService {

    private final LocationProductRepository locationProductRepository;
    private final LocationProductMapper locationProductMapper;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;

    /**
     * Get all location products.
     *
     * @return List of LocationProductDTO containing all location products
     */
    public List<LocationProductDTO> getAllLocationProducts() {
        // Retrieve all location products from the repository and convert them to DTOs
        List<LocationProduct> locationProducts = locationProductRepository.findAll();
        return locationProductMapper.toLocationProductDTOs(locationProducts);
    }

    /**
     * Get location products by warehouse number.
     *
     * @param warehousenr the warehouse number to filter location products
     * @return List of LocationProductDTO containing location products for the specified warehouse
     */
    public List<LocationProductDTO> getLocationProductsByWarehouse(Integer warehousenr) {
        // Retrieve location products by warehouse number and convert them to DTOs
        List<LocationProduct> locationProducts = locationProductRepository.findByWarehousenr(warehousenr);
        return locationProductMapper.toLocationProductDTOs(locationProducts);
    }

    /**
     * Get all locations for product by product number.
     *
     * @param productnr the product number to filter locations
     * @return List of LocationProductDTO containing locations for the specified product
     */
    public List<LocationProductForDetailsDTO> getAllLocationsForProduct(Integer productnr) {
        // Retrieve locations for the specified product number and convert them to DTOs
        List<LocationProduct> locationProducts = locationProductRepository.findLocationByProductnr(productnr);
        return locationProductMapper.toLocationProductForDetailsDTOs(locationProducts);
    }

    /**
     * Create a new location product.
     *
     * @param locationProductDTO the location product data to create
     * @return LocationProductDTO containing the created location product
     */
    public LocationProductDTO createLocationProduct(LocationProductDTO locationProductDTO) {

        // Check if the warehouse and product exist
        if (!warehouseRepository.existsById(locationProductDTO.getWarehousenr())) {
            throw new EntityNotFoundException("Warehouse not found with ID: " + locationProductDTO.getWarehousenr());
        }

        if (!productRepository.existsById(locationProductDTO.getProduct().getProductnr())) {
            throw new EntityNotFoundException("Product not found with ID: " + locationProductDTO.getProduct().getProductnr());
        }

        Location location = locationRepository.findByRacknrAndPlacenrAndWarehouse(
                locationProductDTO.getRacknr(),
                locationProductDTO.getPlacenr(),
                locationProductDTO.getWarehousenr())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with given rack, placenr and warehouse"));

        // Check if the product already exists in the location
        if (locationProductRepository.existsByRacknrAndPlacenrAndWarehouseAndProduct(
                locationProductDTO.getRacknr(),
                locationProductDTO.getPlacenr(),
                locationProductDTO.getWarehousenr(),
                locationProductDTO.getProduct().getProductnr())) {
            throw new IllegalArgumentException("Product already exists in the specified location");
        }

        LocationProduct locationProduct = new LocationProduct();
        LocationProductId locationProductId = new LocationProductId();
        LocationIdForLocationProduct locationIdForLocationProduct = new LocationIdForLocationProduct();

        locationProduct.setQuantity(locationProductDTO.getQuantity());

        locationProductId.setProduct(productRepository.findById(locationProductDTO.getProduct().getProductnr())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + locationProductDTO.getProduct().getProductnr())));

        locationIdForLocationProduct.setWarehouse(warehouseRepository.findById(locationProductDTO.getWarehousenr())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + locationProductDTO.getWarehousenr())));
        locationIdForLocationProduct.setRacknr(locationProductDTO.getRacknr());
        locationIdForLocationProduct.setPlacenr(locationProductDTO.getPlacenr());

        locationProductId.setLocationIdForLocationProduct(locationIdForLocationProduct);

        locationProduct.setLocationProductId(locationProductId);

        LocationProduct savedLocationProduct = locationProductRepository.save(locationProduct);

        return locationProductMapper.toLocationProductDTO(savedLocationProduct);
    }

    /**
     * Update the quantity of a location product.
     *
     * @param warehousenr the warehouse number
     * @param racknr the rack number
     * @param placenr the place number
     * @param productnr the product number
     * @param quantity the new quantity
     * @return LocationProductDTO containing the updated location product
     */
    public LocationProductDTO updateLocationProductQuantity(Integer warehousenr, Integer racknr,
                                                            Integer placenr, Integer productnr, BigDecimal quantity) {

        // Find the existing location product
        LocationProduct locationProduct = locationProductRepository
                .findByRacknrAndPlacenrAndWarehouseAndProduct(warehousenr, racknr, placenr, productnr)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Location product not found at warehouse %d, rack %d, place %d for product %d",
                                warehousenr, racknr, placenr, productnr)));

        // Update the quantity
        locationProduct.setQuantity(quantity);

        // Save and return the updated location product
        LocationProduct savedLocationProduct = locationProductRepository.save(locationProduct);
        return locationProductMapper.toLocationProductDTO(savedLocationProduct);
    }

    /**
     * Delete a location product by its IDs
     *
     * @param racknr the rack number of the location
     * @param placenr the place number of the location
     * @param warehousenr the warehouse number
     * @param productnr the product number
     *
     * @throws EntityNotFoundException if the location product does not exist
     */
    public void deleteLocationProduct(Integer racknr, Integer placenr, Integer warehousenr, Integer productnr) {
        // Check if the location product exists
        LocationProduct locationProduct = locationProductRepository.findByRacknrAndPlacenrAndWarehouseAndProduct(
                racknr, placenr, warehousenr, productnr)
                .orElseThrow(() -> new EntityNotFoundException("Location product not found with given parameters"));

        // Delete the location product
        locationProductRepository.delete(locationProduct);
        }

}
