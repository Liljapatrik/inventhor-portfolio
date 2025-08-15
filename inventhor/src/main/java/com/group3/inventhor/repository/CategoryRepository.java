package com.group3.inventhor.repository;


import com.group3.inventhor.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tatiana Fløisbonn
 *
 * The CategoryRepository interface provides methods to interact with the Category entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * The @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
