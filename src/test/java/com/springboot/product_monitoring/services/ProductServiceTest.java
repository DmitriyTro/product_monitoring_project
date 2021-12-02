package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.ProductDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Category;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.mappers.ProductMapper;
import com.springboot.product_monitoring.repositories.CategoryRepository;
import com.springboot.product_monitoring.repositories.ProductRepository;
import com.springboot.product_monitoring.services.impl.ProductServiceImpl;
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
class ProductServiceTest {

	@MockBean
	ProductRepository productRepository;

	@MockBean
	CategoryRepository categoryRepository;

	@MockBean
	ProductMapper productMapper;

	@Captor
	ArgumentCaptor<Product> argumentCaptor;

	@MockBean
	ProductServiceImpl productService;

	Product product;

	ProductDTO productDTO;

	List<Product> products;

	@BeforeEach
	void setUp() {
		productService = new ProductServiceImpl(productRepository, categoryRepository, productMapper);

		product = new Product();
		product.setId(1);
		product.setProductName("testproduct");

		productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setProductName("testproduct");
	}

	@AfterEach
	void tearDown() {

		product = null;
		productDTO = null;
	}

	@Test
	@DisplayName("Should return product by id")
	void findProductById() {

		when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
		when(productMapper.toProductDTO(any(Product.class))).thenReturn(productDTO);

		ProductDTO actualProductDTO = productService.findProductById(1);

		assertThat(actualProductDTO.getId()).isEqualTo(1);
		assertThat(actualProductDTO.getProductName()).isEqualTo("testproduct");
	}

	@Test
	@DisplayName("Should return product by product name")
	void findByProductName() {

		when(productRepository.findByProductName("testproduct")).thenReturn(Optional.ofNullable(product));
		when(productMapper.toProductDTO(any(Product.class))).thenReturn(productDTO);

		ProductDTO actualProductDTO = productService.findByProductName("testproduct");

		assertThat(actualProductDTO.getId()).isEqualTo(1);
		assertThat(actualProductDTO.getProductName()).isEqualTo("testproduct");
	}

	@Test
	@DisplayName("Should return list of products")
	void findAllProducts() {

		PageRequest pageRequest =  PageRequest.of(0, 10);
		products = new ArrayList<>(List.of(product, product));
		Page<Product> productPage = new PageImpl<>(products, pageRequest, products.size());

		when(productRepository.findAll(pageRequest)).thenReturn(productPage);
		Page<ProductDTO> actualProductPage = productService.findAllProducts(pageRequest);

		assertThat(productPage.getContent().get(0).getId()).isEqualTo(1);
		assertThat(productPage.getContent().size()).isEqualTo(2);
		assertThat(actualProductPage.getPageable().getPageSize()).isEqualTo(10);
		assertThat(actualProductPage.getTotalElements()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should delete product by id")
	void deleteById() {

		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		MessageResponse actualProduct = productService.deleteById(product.getId());

		assertEquals("Product deleted successfully!", actualProduct.getMessage());
	}

	@Test
	@DisplayName("Should save product with category")
	void saveProductWithCategory() {

		Category category = new Category();
		category.setId(1);
		category.setCategoryName("testcategory");
		List<Category> categories = new ArrayList<>(List.of(category));

		when(productRepository.findByProductName("testproduct")).thenReturn(Optional.ofNullable(product));
		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		product.setCategories(categories);
		productService.saveProductWithCategory(product);

		verify(productRepository, times(1)).save(argumentCaptor.capture());

		assertThat(argumentCaptor.getValue().getId()).isEqualTo(1);
		assertThat(argumentCaptor.getValue().getProductName()).isEqualTo("testproduct");
		assertThat(argumentCaptor.getValue().getCategories().get(0).getId()).isEqualTo(1);
		assertThat(argumentCaptor.getValue().getCategories().get(0).getCategoryName()).isEqualTo("testcategory");
	}
}