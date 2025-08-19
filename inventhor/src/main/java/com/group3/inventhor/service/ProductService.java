package com.group3.inventhor.service;


import com.group3.inventhor.dto.CategoryDTO;
import com.group3.inventhor.dto.ProductCreateDTO;
import com.group3.inventhor.dto.ProductDTO;
import com.group3.inventhor.mapper.ProductMapper;
import com.group3.inventhor.model.Product;
import com.group3.inventhor.repository.CategoryRepository;
import com.group3.inventhor.repository.LocationProductRepository;
import com.group3.inventhor.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Tatiana Fløisbonn
 *
 * The ProductService class provides methods to manage products in the Inventhor application.
 * It includes methods to get, create, update, and delete products.
 *
 * @Service indicates that this is a service class that contains business logic and interacts with the data access layer.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    // The ProductRepository instance used to interact with the database for product-related operations
    private final ProductRepository productRepository;
    // The ProductMapper instance used to convert between Product and ProductDTO objects
    private final ProductMapper productMapper;
    // The CategoryRepository instance used to interact with the database for category-related operations
    private final CategoryRepository categoryRepository;

    private final LocationProductRepository locationProductRepository;

    private final ProductSupplierService productSupplierService;

    /**
     * Get all products from the database.
     * This method retrieves all products from the product repository and maps them to ProductDTOs.
     *
     * @return a list of ProductDTOs representing all products in the database
     */
    public List<ProductDTO> getAllProducts() {

        List<Product> products = productRepository.findAll();

        List<ProductDTO> productDTOs = productMapper.toProductDTOs(products);

        for (ProductDTO productDTO : productDTOs) {
            productDTO.setQuantity(locationProductRepository.sumQuantityByProductnr(productDTO.getProductnr()).orElse(0)); // Set the quantity for each product
        }

        return productDTOs;
    }

    /**
     * Get a product by its nr.
     * This method retrieves a product from the product repository by its nr and maps it to a ProductDTO.
     *
     * @param productnr the nr of the product to retrieve
     * @return a ProductDTO representing the product with the specified nr
     */
    public ProductDTO getProductById(Integer productnr) {
        Product product = productRepository.findById(productnr)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with nr: " + productnr));
        return productMapper.toProductDTO(product);
    }

    /**
     * Create a new product.
     * This method saves a new product to the product repository after mapping it from a ProductDTO.
     *
     * @param productCreateDTO the ProductCreateDTO representing the product to create
     * @return a ProductDTO representing the created product
     */
    @Transactional
    public ProductDTO createProduct(ProductCreateDTO productCreateDTO) {

        Product product = new Product();
        product.setImage(productCreateDTO.getImage());
        product.setName(productCreateDTO.getName());
        product.setDescription(productCreateDTO.getDescription());

        CategoryDTO categoryDTO = productCreateDTO.getCategory();
        // Check if the category exists in the database
        if (categoryDTO != null && categoryDTO.getCategorynr() != null) {
            categoryRepository.findById(categoryDTO.getCategorynr())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryDTO.getCategorynr()));
            product.setCategory(categoryRepository.getReferenceById(categoryDTO.getCategorynr()));
        } else {
            throw new IllegalArgumentException("Category must be provided for the product.");
        }

        product.setWidth(productCreateDTO.getWidth());
        product.setHeight(productCreateDTO.getHeight());
        product.setDepth(productCreateDTO.getDepth());
        product.setWeight(productCreateDTO.getWeight());
        product.setSellprice(productCreateDTO.getSellprice());
        product.setUnit(productCreateDTO.getUnit());

        Product savedProduct = productRepository.save(product);


        // Add product to supplier
        productSupplierService.createProductForSupplier(
                productCreateDTO.getSuppliernr(),
                product.getProductnr(),
                productCreateDTO.getEmployeenr()
        );

        return productMapper.toProductDTO(savedProduct);
    }


    /**
     * Update an existing product.
     * This method updates an existing product in the product repository with the details from a ProductDTO.
     *
     * @param productnr the nr of the product to update
     * @param productDTO the ProductDTO containing updated product details
     * @return a ProductDTO representing the updated product
     */
    public ProductDTO updateProduct(Integer productnr, ProductDTO productDTO) {

        Product product = productRepository.findById(productnr)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with nr: " + productnr));

        product.setImage(productDTO.getImage());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());

        CategoryDTO categoryDTO = productDTO.getCategory();
        // Check if the category exists in the database
        if (categoryDTO != null && categoryDTO.getCategorynr() != null) {
            categoryRepository.findById(categoryDTO.getCategorynr())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryDTO.getCategorynr()));
            product.setCategory(categoryRepository.getReferenceById(categoryDTO.getCategorynr()));
        } else {
            throw new IllegalArgumentException("Category must be provided for the product.");
        }

        product.setWidth(productDTO.getWidth());
        product.setHeight(productDTO.getHeight());
        product.setDepth(productDTO.getDepth());
        product.setWeight(productDTO.getWeight());
        product.setSellprice(productDTO.getSellprice());
        product.setUnit(productDTO.getUnit());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductDTO(updatedProduct);
    }

    /**
     * Delete a product by its nr.
     * This method deletes a product from the product repository by its nr.
     *
     * @param productnr the nr of the product to delete
     */
    public void deleteProduct(Integer productnr) {
        if (!productRepository.existsById(productnr)) {
            throw new EntityNotFoundException("Product not found with nr: " + productnr);
        }
        productRepository.deleteById(productnr);
    }

}
