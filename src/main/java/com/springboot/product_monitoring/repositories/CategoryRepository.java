package com.springboot.product_monitoring.repositories;

import com.springboot.product_monitoring.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Category findByCategoryName(String categoryName);

	Boolean existsByCategoryName(String categoryName);
}
