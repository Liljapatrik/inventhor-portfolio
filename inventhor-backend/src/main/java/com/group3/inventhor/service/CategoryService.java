package com.group3.inventhor.service;

import com.group3.inventhor.dto.CategoryDTO;
import com.group3.inventhor.mapper.CategoryMapper;
import com.group3.inventhor.model.Category;
import com.group3.inventhor.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The CategoryService class provides methods to manage categories in the Inventhor application.
 * It includes methods to get, update, create, and delete categories.
 *
 * @Service indicates that this is a service class that contains business logic and interacts with the data access layer.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    // This service class is responsible for handling business logic related to categories.
    private final CategoryRepository categoryRepository;
    // The CategoryMapper instance is used to convert between Category and CategoryDTO objects.
    private final CategoryMapper categoryMapper;

    /**
     * Get category by category number.
     *
     * @param categorynr the unique identifier for the category.
     * @return CategoryDTO containing category details.
     */
    public CategoryDTO getCategoryByCategoryNr(Integer categorynr) {
        // Find category by nr, throw exception if not found
        Category category = categoryRepository.findById(categorynr)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return categoryMapper.toCategoryDTO(category);
    }

    /**
     * Get all categories.
     *
     * @return List of CategoryDTO containing all categories.
     */
    public List<CategoryDTO> getAllCategories() {
        // Retrieve all categories from the repository and convert them to DTOs
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toCategoryDTOs(categories);
    }

    /**
     * Create a new category.
     *
     * @param categoryDTO the CategoryDTO containing category details to be created.
     * @return CategoryDTO containing the created category details.
     */
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        Category category = new Category();
        category.setName(categoryDTO.getName());

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDTO(savedCategory);
    }

    /**
     * Update category by category number.
     *
     * @param categorynr the unique identifier for the category to be updated.
     * @param categoryDTO the CategoryDTO containing updated category details.
     * @return CategoryDTO containing updated category details.
     */
    public CategoryDTO updateCategoryByCategoryNr(Integer categorynr, CategoryDTO categoryDTO) {
        // Find category by nr, throw exception if not found
        Category category = categoryRepository.findById(categorynr)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        // Update category fields with values from categoryDTO
        category.setName(categoryDTO.getName());

        // Save updated category to the repository
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDTO(updatedCategory);
    }

    /**
     * Delete a category by category number.
     *
     * @param categorynr the unique identifier for the category to be deleted.
     */
    public void deleteCategoryByCategoryNr(Integer categorynr) {
        // Find category by nr, throw exception if not found
        Category category = categoryRepository.findById(categorynr)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        // Delete the category from the repository
        categoryRepository.delete(category);
    }

}
