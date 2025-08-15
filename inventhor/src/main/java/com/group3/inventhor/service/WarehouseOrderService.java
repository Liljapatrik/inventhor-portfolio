package com.group3.inventhor.service;

import com.group3.inventhor.dto.WarehouseOrderCreateDTO;
import com.group3.inventhor.dto.WarehouseOrderDTO;
import com.group3.inventhor.mapper.WarehouseOrderMapper;
import com.group3.inventhor.model.*;
import com.group3.inventhor.repository.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Nils Patrik Lilja
 */
@Service
@RequiredArgsConstructor
public class WarehouseOrderService {
    private final WarehouseOrderRepository warehouseOrderRepository;
    private final WarehouseOrderMapper warehouseOrderMapper;
    private final SupplierRepository supplierRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final WarehouseOrderProductRepository warehouseOrderProductRepository;
    private final ProductSupplierRepository productSupplierRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Retrieves a WarehouseOrder by its order number.
     * param ordernr The ID of the warehouse order to retrieve.
     * return WarehouseOrderDTO matching the given order number.
     * throws EntityNotFoundException if no WarehouseOrder with the given ID is found.
     */
    public WarehouseOrderDTO getWarehouseOrderById(Integer ordernr) {
        WarehouseOrder warehouseOrder = warehouseOrderRepository
                .findById(ordernr)
                .orElseThrow(() -> new EntityNotFoundException("WarehouseOrder not found"));
        return warehouseOrderMapper.toDTO(warehouseOrder);
    }

    /**
     * Retrieves all WarehouseOrder entities and converts them to DTOs.
     * return List of WarehouseOrderDTO representing all warehouse orders.
     */
    public List<WarehouseOrderDTO> findAllDTOs() {
        List<WarehouseOrder> warehouseOrders = warehouseOrderRepository.findAll();
        return convertToDTOList(warehouseOrders);
    }

    /**
     * Converts a list of WarehouseOrder entities into a list of WarehouseOrderDTOs.
     * param warehouseOrders List of WarehouseOrder entities.
     * return List of WarehouseOrderDTO objects.
     */
    private List<WarehouseOrderDTO> convertToDTOList(List<WarehouseOrder> warehouseOrders) {
        List<WarehouseOrderDTO> warehouseOrderDTOS = new ArrayList<>();

        for (WarehouseOrder warehouseOrder : warehouseOrders) {
            WarehouseOrderDTO warehouseOrderDTO = warehouseOrderMapper.toDTO(warehouseOrder);
            warehouseOrderDTOS.add(warehouseOrderDTO);
        }
        return warehouseOrderDTOS;
    }

    /**
     * Creates a new WarehouseOrder along with its associated products.
     * param dto The data transfer object containing order details and product lines.
     * param employeenr The employee ID performing the operation (used for authorization).
     * return WarehouseOrderDTO representing the newly created warehouse order.
     * throws IllegalArgumentException if employee not found, or if any product is not linked to the specified supplier.
     * throws SecurityException if the employee is not authorized to create orders.
     * throws EntityNotFoundException if referenced Warehouse, Supplier, OrderStatus, or Product is not found.
     */
    @Transactional
    public WarehouseOrderDTO createWarehouseOrder(WarehouseOrderCreateDTO dto, Integer employeenr) {


        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        System.out.println("Employee ID: " + employee.getEmployeenr() + ", Role: " + employee.getRole());

        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete suppliers");
        }
        // GET enitys
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehousenr())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found " + dto.getWarehousenr()));

        Supplier supplier = supplierRepository.findById(dto.getSuppliernr())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        OrderStatus orderStatus = orderStatusRepository.findByNameIgnoreCase("ordered")
                .orElseThrow(() -> new EntityNotFoundException("Status 'ordered' not found"));

        // Check is product is linked to supplier
        for (WarehouseOrderCreateDTO.ProductLine productLine : dto.getProducts()) {
            boolean isLinked = productSupplierRepository.existsByProduct_ProductnrAndSupplier_Suppliernr(productLine.getProductnr(), supplier.getSuppliernr());
            if (!isLinked) {
                throw new IllegalArgumentException("Product " + productLine.getProductnr() + " is not supplied by Supplier " + supplier.getSuppliernr());
            }
        }

        WarehouseOrder warehouseOrder = new WarehouseOrder();
        warehouseOrder.setWarehouse(warehouse);
        warehouseOrder.setSupplier(supplier);
        warehouseOrder.setStatus(orderStatus);
        warehouseOrder.setOrderdate(LocalDateTime.now());
        // Allow deliverydate to be null
        if (dto.getDeliverydate() != null) {
            warehouseOrder.setDeliverydate(dto.getDeliverydate());
        }
        WarehouseOrder savedOrder = warehouseOrderRepository.save(warehouseOrder);

        // Handle every product in an order
        for (WarehouseOrderCreateDTO.ProductLine productLine : dto.getProducts()) {
            // Get product
            Product product = productRepository.findById(productLine.getProductnr())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            // Create WarehouseOrderProduct and set composite key (id)
            WarehouseOrderProduct orderProduct = new WarehouseOrderProduct();

            WarehouseOrderProductId id = new WarehouseOrderProductId();
            id.setOrdernr(savedOrder.getOrdernr());
            id.setProductnr(product.getProductnr());

            orderProduct.setId(id);
            orderProduct.setWarehouseOrder(savedOrder);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(productLine.getQuantity());
            orderProduct.setBuyprice(productLine.getBuyprice());

            warehouseOrderProductRepository.save(orderProduct);
        }
        // Return DTO
        return warehouseOrderMapper.toDTO(savedOrder);
    }

    /**
     * Updates an existing WarehouseOrder's status and delivery date.
     * param ordernr The ID of the warehouse order to update.
     * param dto The WarehouseOrderDTO containing the updated data.
     * param employeenr The employee ID performing the update (used for authorization).
     * return WarehouseOrderDTO representing the updated warehouse order.
     * throws IllegalArgumentException if employee not found, or if the order is in a non-modifiable status.
     * throws SecurityException if the employee is not authorized to update warehouse orders.
     * throws EntityNotFoundException if the warehouse order or status is not found.
     */
    @Transactional
    public WarehouseOrderDTO updateWarehouseOrder(Integer ordernr, WarehouseOrderDTO dto, Integer employeenr) {

        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        System.out.println("Employee ID: " + employee.getEmployeenr() + ", Role: " + employee.getRole());

        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete suppliers");
        }

        WarehouseOrder existingOrder = warehouseOrderRepository.findById(ordernr)
                .orElseThrow(() -> new EntityNotFoundException("Order not found " + ordernr));

        if (existingOrder.getStatus().getName().equalsIgnoreCase("cancelled")) {
            throw new IllegalArgumentException("Order cannot be modified in its current status");
        }

        // Update status and deleiverydate
        if (dto.getStatusnr() != null) {
            OrderStatus newStatus = orderStatusRepository.findById(dto.getStatusnr())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found"));
            existingOrder.setStatus(newStatus);
        }

        if (dto.getDeliverydate() != null) {
            existingOrder.setDeliverydate(dto.getDeliverydate());
        }

        WarehouseOrder updatedOrder = warehouseOrderRepository.save(existingOrder);
        return warehouseOrderMapper.toDTO(updatedOrder);
    }

    /**
     * Deletes a warehouse order and its associated order products.
     * param ordernr The ID of the warehouse order to delete.
     * param employeenr The employee ID performing the deletion (for authorization).
     * throws IllegalArgumentException if the employee is not found.
     * throws SecurityException if the employee is not authorized to delete warehouse orders.
     * throws EntityNotFoundException if the warehouse order to delete is not found.
     */
    @Transactional
    public void deleteWarehouseOrder(Integer ordernr, Integer employeenr) {

        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        System.out.println("Employee ID: " + employee.getEmployeenr() + ", Role: " + employee.getRole());

        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete suppliers");
        }

        WarehouseOrder order = warehouseOrderRepository.findById(ordernr)
                .orElseThrow(() -> new EntityNotFoundException("Order nr " + ordernr + " not found"));

        warehouseOrderProductRepository.deleteAllByWarehouseOrder_Ordernr(ordernr);
        warehouseOrderRepository.delete(order);
    }
}
