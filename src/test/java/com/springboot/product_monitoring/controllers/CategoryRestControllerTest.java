package com.springboot.product_monitoring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.product_monitoring.dto.CategoryDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.services.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = CategoryRestController.class)
class CategoryRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	@Autowired
	private ObjectMapper mapper;

	CategoryDTO oneCategory;

	CategoryDTO twoCategory;

	@BeforeEach
	void setUp() {
		oneCategory = new CategoryDTO();
		oneCategory.setId(1);
		oneCategory.setCategoryName("testing_1");

		twoCategory = new CategoryDTO();
		twoCategory.setId(2);
		twoCategory.setCategoryName("testing_2");
	}

	@AfterEach
	void tearDown() {
		oneCategory = twoCategory = null;
	}

	@Test
	@WithMockUser(roles = "USER")
	void findCategoryById() throws Exception {
		when(categoryService.findCategoryById(anyInt())).thenReturn(oneCategory);

		mockMvc.perform(get("/api/auth/categories/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.categoryName", is("testing_1")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	void findByCategoryName() throws Exception {
		when(categoryService.findByCategoryName(anyString())).thenReturn(oneCategory);

		mockMvc.perform(get("/api/auth/categories/name/{categoryName}", "testing_1")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.categoryName", is("testing_1")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	void findAllCategories() throws Exception {
		PageRequest pageRequest =  PageRequest.of(0, 10);
		List<CategoryDTO> categories = new ArrayList<>(Arrays.asList(oneCategory, twoCategory));
		Page<CategoryDTO> categoriesPage = new PageImpl<>(categories, pageRequest, categories.size());

		when(categoryService.findAllCategories(any(Pageable.class))).thenReturn(categoriesPage);
		mockMvc.perform(get("/api/auth/categories/list")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteById() throws Exception {
		when(categoryService.deleteById(twoCategory.getId())).thenReturn(new MessageResponse("Category deleted successfully!"));

		MvcResult requestResult = mockMvc.perform(delete("/api/auth/categories/delete/{categoryId}",twoCategory.getId())
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		assertEquals(result.substring(12, 42), "Category deleted successfully!");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void saveCategory() throws Exception {
		when(categoryService.saveCategory(any())).thenReturn(oneCategory);

		mockMvc.perform(post("/api/auth/categories/save").with(csrf())
						.content(mapper.writeValueAsString(oneCategory))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.categoryName", is("testing_1")))
				.andExpect(status().isCreated());
	}
}