package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.CategoryDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

	CategoryDTO findCategoryById(int id);

	CategoryDTO findByCategoryName(String categoryName);

	Page<CategoryDTO> findAllCategories(Pageable pageable);

	MessageResponse deleteById(int id);

	CategoryDTO saveCategory(Category category);

	CategoryDTO addProductToCategory(int categoryId, int productId);
}
