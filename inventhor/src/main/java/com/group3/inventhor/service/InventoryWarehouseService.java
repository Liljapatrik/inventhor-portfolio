package com.group3.inventhor.service;

import com.group3.inventhor.dto.InventoryWarehouseDTO;
import com.group3.inventhor.mapper.InventoryWarehouseMapper;
import com.group3.inventhor.model.*;
import com.group3.inventhor.repository.*;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Nils Patrik Lilja
 */
@Service
@RequiredArgsConstructor
public class InventoryWarehouseService {
    private final InventoryWarehouseRepository inventoryWarehouseRepository;
    private final InventoryWarehouseMapper inventoryWarehouseMapper;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final WarehouseOrderRepository warehouseOrderRepository;
    private final WarehouseRepository warehouseRepository;

    /**
     * Retrieves a specific product stored in a warehouse by warehouse number and product number.
     * param warehousenr the ID of the warehouse
     * param productnr the ID of the product
     * return InventoryWarehouseDTO representing the product stock details in the warehouse
     * throws EntityNotFoundException if no matching product is found in the warehouse
     */
    public InventoryWarehouseDTO getProductByWarehouse(Integer warehousenr, Integer productnr) {
        InventoryWarehouse inventoryWarehouse = inventoryWarehouseRepository
                .findById_WarehousenrAndId_Productnr(warehousenr, productnr)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return inventoryWarehouseMapper.toDTO(inventoryWarehouse);
    }

    /**
     * Retrieves all products stored in a specific warehouse by warehouse number.
     * param warehousenr the ID of the warehouse
     * return a list of InventoryWarehouseDTOs representing the stock details of all products in the warehouse
     */
    public List<InventoryWarehouseDTO> getProductsByWarehouse(Integer warehousenr) {
        List<InventoryWarehouse> inventoryWarehouse = inventoryWarehouseRepository.findById_Warehousenr(warehousenr);
        return convertToDTO(inventoryWarehouse);
    }

    /**
     * Converts a list of InventoryWarehouse entities to a list of InventoryWarehouseDTOs.
     * param inventoryWarehouse the list of InventoryWarehouse entities
     * return list of InventoryWarehouseDTOs
     */
    private List<InventoryWarehouseDTO> convertToDTO(List<InventoryWarehouse> inventoryWarehouse) {
        List<InventoryWarehouseDTO> dtoList = new ArrayList<>();
            for (InventoryWarehouse warehouse : inventoryWarehouse) {
                InventoryWarehouseDTO dto = inventoryWarehouseMapper.toDTO(warehouse);
                dtoList.add(dto);
            }
            return dtoList;
    }


    /**
     * Creates a new InventoryWarehouse record linking a warehouse and product with stock levels.
     * param inventoryWarehouseDTO the DTO containing warehouse and product data with stock levels
     * param employeenr the employee number of the user attempting to create the record (for authorization)
     * return the created InventoryWarehouseDTO
     * throws IllegalArgumentException if the employee is not found or the inventory record already exists
     * throws SecurityException if the employee does not have admin authorization
     */
    public InventoryWarehouseDTO createInventoryWarehouse(InventoryWarehouseDTO inventoryWarehouseDTO, Integer employeenr) {

        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to create inventory warehouse");
        }

        Integer warehousenr = inventoryWarehouseDTO.getWarehousenr();
        Integer productnr = inventoryWarehouseDTO.getProductnr();


        InventoryWarehouseId inventoryWarehouseId = new InventoryWarehouseId(warehousenr, productnr);


        if (inventoryWarehouseRepository.existsById(inventoryWarehouseId)) {
            throw new IllegalArgumentException("Inventory warehouse already exists");
        }

        Warehouse warehouse = warehouseRepository.findById(warehousenr)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));

        Product product = productRepository.findById(productnr)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        InventoryWarehouseId id = new InventoryWarehouseId(warehousenr, productnr);

        if (inventoryWarehouseRepository.existsById(id)) {
            throw new SecurityException("Inventory warehouse already exists");
        }

        InventoryWarehouse link = new InventoryWarehouse();
        link.setId(id);
        link.setProduct(product);
        link.setWarehouse(warehouse);
        link.setMaxstocklvl(inventoryWarehouseDTO.getMaxstocklvl());
        link.setMinstocklvl(inventoryWarehouseDTO.getMinstocklvl());

        inventoryWarehouseRepository.save(link);

        return inventoryWarehouseMapper.toDTO(link);
    }


    /**
     * Updates an existing InventoryWarehouse record with new stock levels.
     * param warehousenr the warehouse number part of the composite key
     * param productnr the product number part of the composite key
     * param updatedDTO the DTO containing updated max and min stock levels
     * param employeenr the employee number performing the update (authorization check)
     * return the updated InventoryWarehouseDTO
     * throws IllegalArgumentException if the employee or inventory warehouse is not found
     * throws SecurityException if the employee is not authorized to update
     */
    public InventoryWarehouseDTO updateInventoryWarehouse(Integer warehousenr, Integer productnr, InventoryWarehouseDTO updatedDTO, Integer employeenr) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to update inventory warehouse");
        }

        Optional<InventoryWarehouse> inventoryOptional = inventoryWarehouseRepository
                .findById_WarehousenrAndId_Productnr(warehousenr, productnr);

        if (inventoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Inventory warehouse not found");
        }

        InventoryWarehouse inventoryWarehouse = inventoryOptional.get();

        inventoryWarehouse.setMaxstocklvl(updatedDTO.getMaxstocklvl());
        inventoryWarehouse.setMinstocklvl(updatedDTO.getMinstocklvl());

        inventoryWarehouseRepository.save(inventoryWarehouse);

        return inventoryWarehouseMapper.toDTO(inventoryWarehouse);
    }



    /**
     * Deletes an InventoryWarehouse record identified by warehouse number and product number.
     * param warehousenr the warehouse number part of the composite key
     * param productnr the product number part of the composite key
     * param employeenr the employee number performing the deletion (authorization check)
     * return an Optional containing the deleted InventoryWarehouseDTO if deletion was successful; otherwise, an empty Optional
     * throws IllegalArgumentException if the employee is not found
     * throws SecurityException if the employee is not authorized to delete
     */
    public Optional<InventoryWarehouseDTO> deleteInventoryWarehouse(Integer warehousenr, Integer productnr, Integer employeenr) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete inventory warehouse");
        }

        Optional<InventoryWarehouse> optionalInventoryWarehouse = inventoryWarehouseRepository.findById_WarehousenrAndId_Productnr(warehousenr, productnr);
        if (optionalInventoryWarehouse.isEmpty()) {
            return Optional.empty();
        }

        InventoryWarehouse inventoryWarehouse = optionalInventoryWarehouse.get();
        inventoryWarehouseRepository.delete(inventoryWarehouse);

        InventoryWarehouseDTO deletedInventoryWarehouseDTO = inventoryWarehouseMapper.toDTO(inventoryWarehouse);
        return Optional.of(deletedInventoryWarehouseDTO);
    }
}
