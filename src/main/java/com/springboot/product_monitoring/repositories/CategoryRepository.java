package com.springboot.product_monitoring.repositories;

import com.springboot.product_monitoring.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Optional<Category> findByCategoryName(String categoryName);

	Boolean existsByCategoryName(String categoryName);
}
