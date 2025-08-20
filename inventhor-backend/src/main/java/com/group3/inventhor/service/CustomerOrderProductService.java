package com.group3.inventhor.service;

import com.group3.inventhor.dto.CustomerOrderProductDTO;
import com.group3.inventhor.mapper.CustomerOrderProductMapper;
import com.group3.inventhor.model.CustomerOrderProduct;
import com.group3.inventhor.model.CustomerOrderProductId;
import com.group3.inventhor.repository.CustomerOrderProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerOrderProductService class provides methods to manage customer order products for the Inventhor application.
 */

@Service
@RequiredArgsConstructor
public class CustomerOrderProductService {
    private final CustomerOrderProductRepository repository;
    private final CustomerOrderProductMapper customerOrderProductMapper;

    /**
     * Get all Order products
     */
    public List<CustomerOrderProductDTO> getAllOrderProducts() {
        return customerOrderProductMapper.toCustomerOrderProductDTOs(repository.findAll());
    }

    /**
     * Get Products by order
     */
    public List<CustomerOrderProductDTO> getProductsByOrder(Integer ordernr) {
        return customerOrderProductMapper.toCustomerOrderProductDTOs(repository.findByOrdernr(ordernr));
    }

    /**
     * Get Order product
     */
    public CustomerOrderProductDTO getOrderProduct(Integer ordernr, Integer productnr) {
        CustomerOrderProductId id = new CustomerOrderProductId();
        id.setOrdernr(ordernr);
        id.setProductnr(productnr);
        CustomerOrderProduct entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderProduct not found"));
        return customerOrderProductMapper.toCustomerOrderProductDTO(entity);
    }

    /**
     * Add products to an order
     */
    public CustomerOrderProductDTO createOrderProduct(CustomerOrderProductDTO dto) {
        CustomerOrderProduct entity = customerOrderProductMapper.toCustomerOrderProduct(dto);
        return customerOrderProductMapper.toCustomerOrderProductDTO(repository.save(entity));
    }

    /**
     * Update order products
     */
    public CustomerOrderProductDTO updateOrderProduct(Integer ordernr, Integer productnr, CustomerOrderProductDTO dto) {
        CustomerOrderProductId id = new CustomerOrderProductId();
        id.setOrdernr(ordernr);
        id.setProductnr(productnr);
        CustomerOrderProduct entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderProduct not found"));
        entity.setQuantity(dto.getQuantity());
        return customerOrderProductMapper.toCustomerOrderProductDTO(repository.save(entity));
    }

    /**
     * Delete order products
     */
    public void deleteOrderProduct(Integer ordernr, Integer productnr) {
        CustomerOrderProductId id = new CustomerOrderProductId();
        id.setOrdernr(ordernr);
        id.setProductnr(productnr);
        repository.deleteById(id);
    }

    /**
     * @author Tatiana Fløisbonn
     *
     * Delete all order products by order number
     *
     * @param ordernr the order number for which to delete all products
     * @throws EntityNotFoundException if no order products are found for the given order number
     */
    public void deleteOrderProductsByOrder(Integer ordernr) {
        List<CustomerOrderProduct> orderProducts = repository.findByOrdernr(ordernr);
        if (orderProducts.isEmpty()) {
            throw new EntityNotFoundException("No order products found for order number: " + ordernr);
        }
        repository.deleteAll(orderProducts);
    }
}
