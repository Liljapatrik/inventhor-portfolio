package com.group3.inventhor.service;

import com.group3.inventhor.dto.ProductSupplierDTO;
import com.group3.inventhor.dto.ProductSupplierGetSuppliersDTO;
import com.group3.inventhor.mapper.ProductSupplierMapper;
import com.group3.inventhor.model.*;
import com.group3.inventhor.repository.EmployeeRepository;
import com.group3.inventhor.repository.ProductRepository;
import com.group3.inventhor.repository.ProductSupplierRepository;
import com.group3.inventhor.repository.SupplierRepository;
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
public class ProductSupplierService {

    private final ProductSupplierRepository productSupplierRepository;
    private final ProductSupplierMapper productSupplierMapper;
    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    /**
     * Retrieves all products linked to a specific supplier.
     * param suppliernr the supplier ID
     * return a list of ProductSupplierDTO representing the products supplied by the supplier
     */
    public List<ProductSupplierDTO> getProductBySupplier(Integer suppliernr) {
        List<ProductSupplier> productSuppliers = productSupplierRepository.findBySupplier_Suppliernr(suppliernr);
        return convertToDTOList(productSuppliers);
    }

    /**
     * Converts a list of ProductSupplier entities to a list of ProductSupplierDTOs.
     * param productSuppliers the list of ProductSupplier entities
     * return a list of corresponding ProductSupplierDTOs
     */
    private List<ProductSupplierDTO> convertToDTOList(List<ProductSupplier> productSuppliers) {
        List<ProductSupplierDTO> dtoList = new ArrayList<>();

        for (ProductSupplier ps : productSuppliers) {
            ProductSupplierDTO dto = productSupplierMapper.toDTO(ps);
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * Converts a single ProductSupplier entity to its DTO representation.
     * param productSupplier the ProductSupplier entity
     * return the corresponding ProductSupplierDTO
     */
    private ProductSupplierDTO convertToDTO(ProductSupplier productSupplier) {
        return productSupplierMapper.toDTO(productSupplier);
    }

    /**
     * Retrieves a specific product supplied by a given supplier using both supplier ID and product ID.
     * param suppliernr the supplier ID
     * param productnr the product ID
     * return the ProductSupplierDTO representing the specific product-supplier relationship
     * throws EntityNotFoundException if no matching product is found for the supplier
     */
    public ProductSupplierDTO getProductBySupplierById(Integer suppliernr, Integer productnr) {
        ProductSupplier productSupplier = productSupplierRepository
                .findBySupplier_SuppliernrAndProduct_Productnr(suppliernr, productnr)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for given supplier"));
        return productSupplierMapper.toDTO(productSupplier);
    }

    /**
     * @author Tatiana Fløisbonn
     *
     * Get all suppliers for a product.
     *
     * @param productnr The product number to find suppliers for.
     * @return list of ProductSupplierDTOs containing supplier information for the specified product.
     */
    public List<ProductSupplierGetSuppliersDTO> getSuppliersByProduct(Integer productnr) {
        // Check if product exists
        List<ProductSupplier> productSuppliers = productSupplierRepository.findByProduct_Productnr(productnr);
        // If no suppliers found, throw an exception
        if (productSuppliers.isEmpty()) {
            throw new EntityNotFoundException("No suppliers found for the given product");
        }
        // Map ProductSupplier entities to ProductSupplierGetSuppliersDTO
        List<ProductSupplierGetSuppliersDTO> suppliers = new ArrayList<>();
        // Loop through each ProductSupplier and convert to DTO
        for (ProductSupplier ps : productSuppliers) {
            suppliers.add(productSupplierMapper.toGetSuppliersDTO(ps));
        }
        // Return the list of suppliers
        return suppliers;
    }

    /**
     * Creates a new link between a product and a supplier.
     * Only employees with role ID 1 are authorized to perform this action.
     * param suppliernr the supplier ID
     * param productnr the product ID
     * param employeenr the employee ID performing the action
     * return a ProductSupplierDTO representing the newly created link
     * throws IllegalArgumentException if employee, product, or supplier is not found,
     * or if the link already exists
     * throws SecurityException if the employee is not authorized
     */
    public ProductSupplierDTO createProductForSupplier(Integer suppliernr, Integer productnr, Integer employeenr ) {
        // Get employee with Optional
        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete suppliers");
        }

        // Check if product and supplier exists
        Product product = productRepository.findById(productnr)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Supplier supplier = supplierRepository.findById(suppliernr)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        ProductSupplierId id = new ProductSupplierId(productnr, suppliernr);

        if (productSupplierRepository.existsById(id)) {
            throw new IllegalArgumentException("This product is already linked to this supplier");
        }

        ProductSupplier link = new ProductSupplier();
        link.setId(id);
        link.setProduct(product);
        link.setSupplier(supplier);

        productSupplierRepository.save(link);

        return new ProductSupplierDTO(productnr, null, suppliernr, null, null, null);
    }


    /**
     * Deletes the link between a product and a supplier by their IDs.
     * Only employees with role ID 1 are authorized to perform this action.
     * param suppliernr the supplier ID
     * param productnr the product ID
     * param employeenr the employee ID performing the action
     * return an Optional containing the deleted ProductSupplierDTO if found and deleted,
     * otherwise Optional.empty() if the link does not exist
     * throws IllegalArgumentException if the employee is not found
     * throws SecurityException if the employee is not authorized
     */
    public Optional<ProductSupplierDTO> deleteProductBySupplierById(Integer suppliernr, Integer productnr, Integer employeenr) {
        // Get employee with Optional
        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete suppliers");
        }

        Optional<ProductSupplier> optionalProductSupplier = productSupplierRepository.findBySupplier_SuppliernrAndProduct_Productnr(suppliernr, productnr);
        if (optionalProductSupplier.isEmpty()) {
            return Optional.empty();
        }
        ProductSupplier productSupplier = optionalProductSupplier.get();
        // Delete product by supplier
        productSupplierRepository.delete(productSupplier);

        // Convert to DTO  and return
        ProductSupplierDTO deletedProductDTO = convertToDTO(productSupplier);
        return Optional.of(deletedProductDTO);
    }
}
