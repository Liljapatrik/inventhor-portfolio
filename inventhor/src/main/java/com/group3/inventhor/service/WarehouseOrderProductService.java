package com.group3.inventhor.service;


import com.group3.inventhor.dto.WarehouseOrderProductDTO;
import com.group3.inventhor.mapper.WarehouseOrderProductMapper;
import com.group3.inventhor.model.*;
import com.group3.inventhor.repository.WarehouseOrderProductRepository;
import com.group3.inventhor.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Nils Patrik Lilja
 */
@Service
@RequiredArgsConstructor
public class WarehouseOrderProductService {
    private final WarehouseOrderProductRepository warehouseOrderProductRepository;
    private final ProductRepository productRepository;
    private final WarehouseOrderProductMapper warehouseOrderProductMapper;

    /**
     * Retrieves a WarehouseOrderProduct by product number and order number.
     * param productnr The product number to search for.
     * param ordernr The order number to search for.
     * return WarehouseOrderProductDTO matching the given product and order numbers.
     * throws EntityNotFoundException if no matching WarehouseOrderProduct is found.
     */
    public WarehouseOrderProductDTO getProductByOrder(Integer productnr, Integer ordernr ) {
        WarehouseOrderProduct warehouseOrderProduct = warehouseOrderProductRepository
                .findByWarehouseOrder_OrdernrAndProduct_Productnr(productnr, ordernr)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return warehouseOrderProductMapper.toDTO(warehouseOrderProduct);
    }

    /**
     * Retrieves all WarehouseOrderProduct entries for a specific order number.
     * param ordernr The order number to filter WarehouseOrderProducts.
     * return List of WarehouseOrderProductDTOs associated with the given order number.
     */
    public List<WarehouseOrderProductDTO> getProductByOrder(Integer ordernr) {
        List<WarehouseOrderProduct> warehouseOrderProducts = warehouseOrderProductRepository.findByWarehouseOrder_Ordernr(ordernr);
        return convertToDTOList(warehouseOrderProducts);
    }

    /**
     * Converts a list of WarehouseOrderProduct entities into a list of DTOs.
     * param warehouseOrderProducts List of WarehouseOrderProduct entities.
     * return List of WarehouseOrderProductDTOs.
     */
    private List<WarehouseOrderProductDTO> convertToDTOList(List<WarehouseOrderProduct> warehouseOrderProducts) {
        List<WarehouseOrderProductDTO> dtoList = new ArrayList<>();
            for (WarehouseOrderProduct warehouseOrderProduct : warehouseOrderProducts) {
                WarehouseOrderProductDTO dto = warehouseOrderProductMapper.toDTO(warehouseOrderProduct);
                dtoList.add(dto);
            }
            return dtoList;

    }
}
