package com.group3.inventhor.service;


import com.group3.inventhor.dto.AddressDTO;
import com.group3.inventhor.dto.WarehouseDTO;
import com.group3.inventhor.mapper.WarehouseMapper;
import com.group3.inventhor.model.Address;
import com.group3.inventhor.model.Employee;
import com.group3.inventhor.model.Warehouse;
import com.group3.inventhor.repository.AddressRepository;
import com.group3.inventhor.repository.EmployeeRepository;
import com.group3.inventhor.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author Furo Muktar Eshetu
* warehouseservice class for managing business logic related to warehouses.
* Handles create, update, read, and delete operations.
*/

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final AddressRepository addressRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * find a single warehouse by its id
     */

    public WarehouseDTO getWarehouseById(Integer id){
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse npt found"));
        return warehouseMapper.toDTO(warehouse);
    }

    /**
     * retrive all warehouse from the database
     */

    public List<WarehouseDTO> findAll(){
        return warehouseRepository.findAll().stream().map(warehouseMapper::toDTO).toList();
    }

    /**
     * create a new warehouse
     * check role of employee , reuses or create address
     */

    public WarehouseDTO createWarehouse(WarehouseDTO dto, Integer employeenr){
        verifyAdmin(employeenr);

        Address address = resolveOrCreateAddress(dto.getAddress());

        Warehouse warehouse = new Warehouse();
        warehouse.setName(dto.getName());
        warehouse.setAddress(address);

        Warehouse saved = warehouseRepository.save(warehouse);
        return warehouseMapper.toDTO(saved);
    }

    /**
     * update an exsisting warehouse. returns optional.empty if warehouse not found
     */

    public Optional<WarehouseDTO> updateWarehouse(Integer id, WarehouseDTO dto, Integer employeenr){
        verifyAdmin(employeenr);

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isEmpty()) return Optional.empty();

        Warehouse warehouse=optionalWarehouse.get();
        warehouse.setName(dto.getName());

        if (dto.getAddress() != null){
            warehouse.setAddress(resolveOrCreateAddress(dto.getAddress()));
        }

        Warehouse updated = warehouseRepository.save(warehouse);
        return Optional.of(warehouseMapper.toDTO(updated));
    }

    /**
     * Delete a warehouse by ID. Returns Optional.empty if not found.
     */

    public Optional<WarehouseDTO> deleteWarehouse(Integer id, Integer employeenr){
        verifyAdmin(employeenr);

        Optional <Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isEmpty()) return Optional.empty();

        Warehouse warehouse=optionalWarehouse.get();
        warehouseRepository.delete(warehouse);
        return Optional.of(warehouseMapper.toDTO(warehouse));
    }

    /**
     *  Utility to find or create address by matching fields.
     */

    private Address resolveOrCreateAddress(AddressDTO dto){
        return addressRepository.findByCountryAndCityAndStreetAndPostcode(
                dto.getStreet(), dto.getPostcode(), dto.getCity(), dto.getCountry()
        ).orElseGet(() -> {
            Address address = new Address();
            address.setStreet(dto.getStreet());
            address.setPostcode(dto.getPostcode());
            address.setCity(dto.getCity());
            address.setCountry(dto.getCountry());
            return addressRepository.save(address);
        });
    }

    /**
     * Validates that the employee has admin role (rolenr == 1)
     */

    private void verifyAdmin(Integer employeenr){
        Employee employee = employeeRepository.findById(employeenr)
                .orElseThrow(()-> new IllegalArgumentException("Employee not found"));
        if (employee.getRole() == null || employee.getRole().getRolenr()!=1){
            throw new SecurityException("You are not authorized to manage warehouses");
        }
    }
}