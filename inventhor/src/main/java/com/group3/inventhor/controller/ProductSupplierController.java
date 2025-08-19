package com.group3.inventhor.controller;


import com.group3.inventhor.dto.ProductSupplierDTO;
import com.group3.inventhor.dto.ProductSupplierGetSuppliersDTO;
import com.group3.inventhor.service.ProductSupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


/**
 * @author Nils Patrik Lilja
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/product-suppliers")
@Tag(name="Productsupplier Controller", description = "API for managing productsuppliers")
@RequiredArgsConstructor
public class ProductSupplierController {

    private final ProductSupplierService productSupplierService;

    /**
     * GET all products from a specific supplier
     * GetMapping to handle HTTP GET request for retrieving all products linked to a supplier
     * Takes supplier number as a path variable
     * Calls service to fetch all products associated with the given supplier ID
     * Returns 200 OK with a list of products if any are found
     * Returns 204 No Content if the supplier has no associated products*/
    @GetMapping("/products-by-supplier/{suppliernr}")
    public ResponseEntity<List<ProductSupplierDTO>> getProductBySupplier(
            @PathVariable Integer suppliernr) {

        List<ProductSupplierDTO> products = productSupplierService.getProductBySupplier(suppliernr);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * GET specific product from a supplier by supplier and product ID
     * GetMapping to handle HTTP GET request for retrieving a specific product tied to a supplier
     * Takes supplier number and product number as path variables
     * Calls service to fetch the product using both supplier and product ID
     * Returns 200 OK with the product if found
     * Returns 404 Not Found if the product does not exist for the given supplier
     */
    @GetMapping("/product-by-supplier/{suppliernr}/{productnr}")
    public ResponseEntity<ProductSupplierDTO> getProductBySupplierAndProductId(
            @PathVariable Integer suppliernr,
            @PathVariable Integer productnr) {

        ProductSupplierDTO productSupplierDTO = productSupplierService.getProductBySupplierById(suppliernr, productnr);
        if (productSupplierDTO != null) {
            return new ResponseEntity<>(productSupplierDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @author Tatiana Fløisbonn
     *
     * Get all suppliers for a product.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve all suppliers for a product.
     *
     * @param productnr the unique identifier for the product
     * @return ResponseEntity containing a list of ProductSupplierDTO with details of all suppliers for the product.
     */
    @GetMapping("/suppliers-by-product/{productnr}")
    public ResponseEntity<List<ProductSupplierGetSuppliersDTO>> getSuppliersByProduct(@PathVariable Integer productnr) {
        List<ProductSupplierGetSuppliersDTO> suppliers = productSupplierService.getSuppliersByProduct(productnr);
        return ResponseEntity.ok(suppliers);
    }


    /**
     * CREATE new product for a supplier
     * PostMapping to handle HTTP POST request to add a new product linked to a supplier
     * Only accessible by users with ADMIN role
     * Takes product and supplier information in the request body as a ProductSupplierDTO
     * Employee number is passed as a request parameter
     * Calls service to create a new product-supplier relation using supplier number and product number from the DTO
     * Returns 201 Created with the created product if successful, otherwise returns appropriate HTTP status codes
     */
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(
            @RequestBody ProductSupplierDTO productSupplierDTO,
            @RequestParam Integer employeenr) {
        try {
            ProductSupplierDTO createdProduct = productSupplierService.createProductForSupplier(
                    productSupplierDTO.getSuppliernr(),productSupplierDTO.getProductnr(), employeenr);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (SecurityException securityException) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE specific product from a supplier
     * DeleteMapping to handle HTTP DELETE request for a specific product tied to a supplier
     * Only accessible by users with ADMIN role
     * Takes supplier number and product number as path variables, and employee number as a request parameter
     * Calls service to delete the product based on supplier and product ID
     * Returns 200 OK with deleted product if successful
     * The method handles various exceptions and returns appropriate HTTP status codes
     */
    @DeleteMapping("/product-by-supplier/{suppliernr}/{productnr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProductBySupplierAndProductId(
            @PathVariable Integer suppliernr,
            @PathVariable Integer productnr,
            @RequestParam Integer employeenr) {
        try {
            ProductSupplierDTO deletedProduct = productSupplierService.deleteProductBySupplierById(suppliernr, productnr, employeenr)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
            return ResponseEntity.ok(deletedProduct);
        } catch (SecurityException securityException) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
