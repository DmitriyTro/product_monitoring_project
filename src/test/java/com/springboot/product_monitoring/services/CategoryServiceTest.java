package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.CategoryDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Category;
import com.springboot.product_monitoring.mappers.CategoryMapper;
import com.springboot.product_monitoring.repositories.CategoryRepository;
import com.springboot.product_monitoring.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategoryServiceTest {

	@MockBean
	CategoryRepository categoryRepository;

	@MockBean
	CategoryMapper categoryMapper;

	@Captor
	ArgumentCaptor<Category> argumentCaptor;

	@MockBean
	CategoryServiceImpl categoryService;

	Category category;

	CategoryDTO categoryDTO;

	List<Category> categories;

	@BeforeEach
	void setUp() {
		categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);

		category = new Category();
		category.setId(1);
		category.setCategoryName("testcategory");

		categoryDTO = new CategoryDTO();
		categoryDTO.setId(1);
		categoryDTO.setCategoryName("testcategory");
	}

	@AfterEach
	void tearDown() {

		category = null;
		categoryDTO = null;
	}

	@Test
	@DisplayName("Should return category by id")
	void findCategoryById() {

		when(categoryRepository.findById(1)).thenReturn(Optional.ofNullable(category));
		when(categoryMapper.toCategoryDTO(any(Category.class))).thenReturn(categoryDTO);

		CategoryDTO actualCategoryDTO = categoryService.findCategoryById(1);

		assertThat(actualCategoryDTO.getId()).isEqualTo(1);
		assertThat(actualCategoryDTO.getCategoryName()).isEqualTo("testcategory");
	}

	@Test
	@DisplayName("Should return category by category name")
	void findByCategoryName() {

		when(categoryRepository.findByCategoryName("testcategory")).thenReturn(Optional.ofNullable(category));
		when(categoryMapper.toCategoryDTO(any(Category.class))).thenReturn(categoryDTO);

		CategoryDTO actualCategoryDTO = categoryService.findByCategoryName("testcategory");

		assertThat(actualCategoryDTO.getId()).isEqualTo(1);
		assertThat(actualCategoryDTO.getCategoryName()).isEqualTo("testcategory");
	}

	@Test
	@DisplayName("Should return list of categories")
	void findAllCategories() {

		PageRequest pageRequest =  PageRequest.of(0, 10);
		categories = new ArrayList<>(List.of(category, category));
		Page<Category> categoryPage = new PageImpl<>(categories, pageRequest, categories.size());

		when(categoryRepository.findAll(pageRequest)).thenReturn(categoryPage);
		Page<CategoryDTO> actualCategoryPage = categoryService.findAllCategories(pageRequest);

		assertThat(categoryPage.getContent().get(0).getId()).isEqualTo(1);
		assertThat(categoryPage.getContent().size()).isEqualTo(2);
		assertThat(actualCategoryPage.getPageable().getPageSize()).isEqualTo(10);
		assertThat(actualCategoryPage.getTotalElements()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should delete category by id")
	void deleteById() {

		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		MessageResponse actualCategory = categoryService.deleteById(category.getId());

		assertEquals("Category deleted successfully!", actualCategory.getMessage());
	}

	@Test
	@DisplayName("Should save category")
	void saveCategory() {

		when(categoryRepository.findByCategoryName("testcategory")).thenReturn(Optional.of(category));

		categoryService.saveCategory(category);
		verify(categoryRepository, times(1)).save(argumentCaptor.capture());

		assertThat(argumentCaptor.getValue().getId()).isEqualTo(1);
		assertThat(argumentCaptor.getValue().getCategoryName()).isEqualTo("testcategory");
	}
}