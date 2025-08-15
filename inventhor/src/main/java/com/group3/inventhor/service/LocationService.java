package com.group3.inventhor.service;


import com.group3.inventhor.dto.LocationDTO;
import com.group3.inventhor.dto.WarehouseDTO;
import com.group3.inventhor.mapper.LocationMapper;
import com.group3.inventhor.model.Location;
import com.group3.inventhor.model.LocationId;
import com.group3.inventhor.repository.LocationRepository;
import com.group3.inventhor.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Furo Muktar Eshetu
 *
 * The LocationService class provides methods to manage locations in the Inventhor application.
 * It interacts with the LocationRepository and WarehouseRepository to perform CRUD operations on locations.
 *
 * @Service annotation indicates that this class is a service component in the Spring context.
 * @RequiredArgsConstructor is a Lombok annotation that generates a constructor with required arguments (final fields).
 */
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final WarehouseRepository warehouseRepository;

    /**
     * Get all locations.
     *
     * @return List of LocationDTO containing all locations.
     */
    public List<LocationDTO> getAllLocations() {
        // Retrieve all locations from the repository and convert them to DTOs
        List<Location> locations = locationRepository.findAll();
        return locationMapper.toLocationDTOs(locations);
    }

    /**
     * Create a new location
     *
     * @param locationDTO the location data to create
     * @return LocationDTO containing the created location
     */
    public LocationDTO createLocation(LocationDTO locationDTO) {

        Location location = new Location();
        LocationId locationId = new LocationId();

        // Check if the location already exists
        if (locationRepository.existsByRacknrAndPlacenrAndWarehouse(locationDTO.getRacknr(), locationDTO.getPlacenr(), locationDTO.getWarehousenr())) {
            throw new EntityNotFoundException("Location already exists with racknr: " + locationDTO.getRacknr() + ", placenr: " + locationDTO.getPlacenr() + " in warehouse: " + locationDTO.getWarehousenr());
        }

        locationId.setWarehouse(warehouseRepository.findById(locationDTO.getWarehousenr())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with nr: " + locationDTO.getWarehousenr())));

        locationId.setRacknr(locationDTO.getRacknr());
        locationId.setPlacenr(locationDTO.getPlacenr());

        location.setLocationId(locationId);

        Location savedLocation = locationRepository.save(location);
        return locationMapper.toLocationDTO(savedLocation);
    }

}