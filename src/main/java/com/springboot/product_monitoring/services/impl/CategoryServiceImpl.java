package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.dto.CategoryDTO;
import com.springboot.product_monitoring.entities.Category;
import com.springboot.product_monitoring.exceptions.category.CategoryException;
import com.springboot.product_monitoring.exceptions.errors.CategoryErrorType;
import com.springboot.product_monitoring.mappers.CategoryMapper;
import com.springboot.product_monitoring.repositories.CategoryRepository;
import com.springboot.product_monitoring.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
	}

	@Override
	public CategoryDTO findCategoryById(int id) {
		Category result = categoryRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method findCategoryById - no category found by id: {}", id);
			throw new CategoryException(String.format(CategoryErrorType.CATEGORY_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method findCategoryById - category found by id: {}", id);
		return categoryMapper.toCategoryDTO(result);
	}

	@Override
	public CategoryDTO findByCategoryName(String categoryName) {
		Category result = categoryRepository.findByCategoryName(categoryName).orElse(null);

		if (result == null) {
			log.warn("IN method findByCategoryName - no category found by category name: {}", categoryName);
			throw new CategoryException(String.format(CategoryErrorType.CATEGORY_BY_CATEGORY_NAME_NOT_FOUND
					.getDescription(), categoryName));
		}

		log.info("IN method findByCategoryName - category: {} found by category name: {}", result, categoryName);
		return categoryMapper.toCategoryDTO(result);
	}

	@Override
	public Page<CategoryDTO> findAllCategories(Pageable pageable) {
		Page<Category> categoryPage = categoryRepository.findAll(pageable);
		List<CategoryDTO> categoriesDTOs = categoryMapper.toCategoriesDTOs(categoryPage.getContent());

		if (categoryPage.isEmpty()) {
			log.warn("IN method findAllCategories - no categories found");
			throw new CategoryException(CategoryErrorType.CATEGORIES_NOT_FOUND.getDescription());
		}

		log.info("IN method findAllCategories - {} categories found", categoryPage.getTotalElements());
		return new PageImpl<>(categoriesDTOs, pageable, categoryPage.getTotalElements());
	}

	@Override
	public void deleteById(int id) {
		Category result = categoryRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method deleteById - no category found by id: {}", id);
			throw new CategoryException(String.format(CategoryErrorType.CATEGORY_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method deleteById category with id: {} successfully deleted", id);
		categoryRepository.deleteById(id);
	}

	@Override
	public CategoryDTO saveCategory(Category category) {
		if (!categoryRepository.existsByCategoryName(category.getCategoryName())) {
			log.info("IN method saveCategory - category by category name: {} saved successfully", category.getCategoryName());
			return categoryMapper.toCategoryDTO(categoryRepository.save(category));
		} else {
			log.warn("IN method saveCategory - category by category name: {} already exists", category.getCategoryName());
			throw new CategoryException(String.format(CategoryErrorType.CATEGORY_ALREADY_EXISTS.getDescription(),
					category.getCategoryName()));
		}
	}
}