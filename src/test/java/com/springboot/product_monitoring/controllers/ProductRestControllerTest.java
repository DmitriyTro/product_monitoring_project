package com.springboot.product_monitoring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.product_monitoring.dto.ProductDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Category;
import com.springboot.product_monitoring.services.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@ContextConfiguration(classes = ProductRestController.class)
class ProductRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper mapper;

	ProductDTO oneProduct;

	ProductDTO twoProduct;

	@BeforeEach
	void setUp() {
		oneProduct = new ProductDTO();
		oneProduct.setId(1);
		oneProduct.setProductName("testing_1");

		twoProduct = new ProductDTO();
		twoProduct.setId(2);
		twoProduct.setProductName("testing_2");
	}

	@AfterEach
	void tearDown() {
		oneProduct = twoProduct = null;
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Should return product by id")
	void findProductById() throws Exception {
		when(productService.findProductById(anyInt())).thenReturn(oneProduct);

		mockMvc.perform(get("/api/auth/products/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.productName", is("testing_1")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Should return product by product name")
	void findByProductName() throws Exception {
		when(productService.findByProductName(anyString())).thenReturn(oneProduct);

		mockMvc.perform(get("/api/auth/products/name/{productName}", "testing_1")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.productName", is("testing_1")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Should return list of products")
	void findAllProducts() throws Exception {
		PageRequest pageRequest =  PageRequest.of(0, 10);
		List<ProductDTO> products = new ArrayList<>(Arrays.asList(oneProduct, twoProduct));
		Page<ProductDTO> productsPage = new PageImpl<>(products, pageRequest, products.size());

		when(productService.findAllProducts(any(Pageable.class))).thenReturn(productsPage);
		mockMvc.perform(get("/api/auth/products/list")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should delete product by id")
	void deleteById() throws Exception {
		when(productService.deleteById(twoProduct.getId())).thenReturn(new MessageResponse("Product deleted successfully!"));

		MvcResult requestResult = mockMvc.perform(delete("/api/auth/products/delete/{productId}",twoProduct.getId())
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		assertEquals(result.substring(12, 41), "Product deleted successfully!");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should return saved product with category in db")
	void saveProductWithCategory() throws Exception {
		Category category = new Category();
		category.setId(1);
		category.setCategoryName("testing");

		List<Category> categories = new ArrayList<>(List.of(category));
		oneProduct.setCategories(categories);

		when(productService.saveProductWithCategory(any())).thenReturn(oneProduct);

		mockMvc.perform(put("/api/auth/products/save").with(csrf())
						.content(mapper.writeValueAsString(oneProduct))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.productName", is("testing_1")))
				.andExpect(jsonPath("$.categories.[0].categoryName", is("testing")))
				.andExpect(status().isOk());
	}
}