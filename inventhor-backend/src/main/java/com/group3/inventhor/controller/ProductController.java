package com.group3.inventhor.controller;


import com.group3.inventhor.dto.ProductCreateDTO;
import com.group3.inventhor.dto.ProductDTO;
import com.group3.inventhor.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * This controller handles HTTP requests related to products in the Inventhor application.
 * It provides endpoints for creating, retrieving, updating, and deleting products.
 *
 * @CrossOrigin allows cross-origin requests from specified origins, enabling the frontend to communicate with this backend service.
 * @RestController indicates that this class is a RESTful controller, capable of handling HTTP requests and returning responses.
 * @RequestMapping specifies the base URL for all endpoints in this controller, which is "/products".
 * @Tag provides metadata for the controller, which can be used in API documentation tools like Swagger.
 * @RequiredArgsConstructor generates a constructor with required arguments, allowing for dependency injection of the ProductService.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"} )
@RestController
@RequestMapping("/products")
@Tag(name="Product Controller", description = "API for managing products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    /**
     * Get all products.
     * This method retrieves all products from the product service and returns them as a list of ProductDTOs.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve all products.
     *
     * @return ResponseEntity containing a list of ProductDTO with details of all products.
     */
    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Get product by nr.
     * This method retrieves a product by its nr from the product service and returns it as a ProductDTO.
     *
     * @param productnr the unique identifier for the product
     * @return ResponseEntity containing ProductDTO with product details
     *
     * @PathVariable indicates that the method expects a path variable containing the product number.
     */
    @Operation(summary = "Get product by productnr", description = "Retrieve product by productnr")
    @GetMapping("/{productnr}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer productnr) {
        return ResponseEntity.ok(productService.getProductById(productnr));
    }

    /**
     * Create a new product.
     * This method creates a new product using the provided ProductDTO and returns the created product as a ProductDTO.
     *
     * @param productCreateDTO the ProductDTO containing details of the product to be created
     * @return ResponseEntity containing the created ProductDTO
     *
     * @RequestBody indicates that the method expects a request body containing the product data in JSON format.
     */
    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        return ResponseEntity.ok(productService.createProduct(productCreateDTO));
    }

    /**
     * Update an existing product.
     * This method updates an existing product using the provided ProductDTO and returns the updated product as a ProductDTO.
     *
     * @param productnr the unique identifier for the product to be updated
     * @param productDTO the ProductDTO containing updated details of the product
     * @return ResponseEntity containing the updated ProductDTO
     *
     * @PathVariable indicates that the method expects a path variable containing the product number.
     * @RequestBody indicates that the method expects a request body containing the updated product data in JSON format.
     */
    @Operation(summary = "Update an existing product", description = "Update information about a product")
    @PutMapping("/{productnr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer productnr, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(productnr, productDTO));
    }

    /**
     * Delete a product by its nr.
     * This method deletes a product from the product service by its nr.
     *
     * @param productnr the unique identifier for the product to be deleted
     * @return ResponseEntity with no content if the deletion was successful
     *
     * @PathVariable indicates that the method expects a path variable containing the product number.
     */
    @Operation(summary = "Delete product", description = "Delete product from DB")
    @DeleteMapping("/{productnr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productnr) {
        productService.deleteProduct(productnr);
        return ResponseEntity.noContent().build();
    }
}
