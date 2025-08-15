package com.group3.inventhor.controller;


import com.group3.inventhor.dto.CategoryDTO;
import com.group3.inventhor.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * Controller for managing categories in the application.
 * Provides endpoints to create, read, update, and delete categories.
 *
 * @CrossOrigin allows cross-origin requests from specified origins.
 * @RestController indicates that this class is a REST controller.
 * @RequestMapping specifies the base path for all requests handled by this controller.
 * @Tag provides metadata (header and description) for Swagger documentation.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"} )
@RestController
@RequestMapping("/category")
@Tag(name="Category Controller", description = "API for managing categories")
@RequiredArgsConstructor
public class CategoryController {

    // The CategoryService instance used to handle category-related operations
    private final CategoryService categoryService;

    /**
     * Get category by categorynr.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve a category by its unique identifier (categorynr).
     *
     * @param categorynr the unique identifier for the category
     * @return ResponseEntity containing CategoryDTO with category details
     *
     * @RequestParam indicates that the method expects a request parameter containing the category number.
     */
    @Operation(summary = "Get category by categorynr", description = "Retrieve category by categorynr")
    @GetMapping("/{categorynr}")
    public ResponseEntity<CategoryDTO> getCategoryByCategoryNr(@RequestParam Integer categorynr) {
        return ResponseEntity.ok(categoryService.getCategoryByCategoryNr(categorynr));
    }

    /**
     * Get all categories.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve all categories.
     *
     * @return ResponseEntity containing a list of CategoryDTO with all categories
     */
    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Create a new category.
     *
     * @param categoryDTO the CategoryDTO containing category details to be created
     * @return ResponseEntity containing the created CategoryDTO
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @PostMapping handles HTTP POST requests to create a new category.
     *
     * @RequestBody indicates that the method expects a request body containing the category data in JSON format.
     */
    @Operation(summary = "Create a new category", description = "Create a new category with the provided details")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    /**
     * Update category by categorynr.
     *
     * @param categorynr the unique identifier for the category to be updated
     * @param categoryDTO the CategoryDTO containing updated category details
     * @return ResponseEntity containing updated CategoryDTO
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @PutMapping handles HTTP PUT requests to update an existing category.
     *
     * @PathVariable indicates that the method expects a path variable containing the category number.
     * @RequestBody indicates that the method expects a request body containing the updated category data in JSON format.
     */
    @Operation(summary = "Update category by categorynr", description = "Update information about a category by categorynr")
    @PutMapping("/{categorynr}")
    public ResponseEntity<CategoryDTO> updateCategoryByCategoryNr(@PathVariable Integer categorynr, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategoryByCategoryNr(categorynr, categoryDTO));
    }

    /**
     * Delete category by categorynr.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @DeleteMapping handles HTTP DELETE requests to delete a category by its unique identifier (categorynr).
     *
     * @param categorynr the unique identifier for the category to be deleted
     * @return ResponseEntity with no content status
     */
    @Operation(summary = "Delete category by categorynr", description = "Delete a category by categorynr")
    @DeleteMapping("/{categorynr}")
    public ResponseEntity<Void> deleteCategoryByCategoryNr(@PathVariable Integer categorynr) {
        categoryService.deleteCategoryByCategoryNr(categorynr);
        return ResponseEntity.noContent().build();
    }
}
