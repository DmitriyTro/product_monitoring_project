package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.dto.ProductDTO;
import com.springboot.product_monitoring.entities.Category;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.exceptions.category.CategoryException;
import com.springboot.product_monitoring.exceptions.errors.CategoryErrorType;
import com.springboot.product_monitoring.exceptions.errors.ProductErrorType;
import com.springboot.product_monitoring.exceptions.product.ProductException;
import com.springboot.product_monitoring.mappers.ProductMapper;
import com.springboot.product_monitoring.repositories.CategoryRepository;
import com.springboot.product_monitoring.repositories.ProductRepository;
import com.springboot.product_monitoring.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductMapper productMapper;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productMapper = productMapper;
	}

	@Override
	public ProductDTO findProductById(int id) {
		Product result = productRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method findProductById - no product found by id: {}", id);
			throw new ProductException(String.format(ProductErrorType.PRODUCT_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method findProductById - product found by id: {}", id);
		return productMapper.toProductDTO(result);
	}

	@Override
	public ProductDTO findByProductName(String productName) {
		Product result = productRepository.findByProductName(productName).orElse(null);

		if (result == null) {
			log.warn("IN method findByProductName - no product found by product name: {}", productName);
			throw new ProductException(String.format(ProductErrorType.PRODUCT_BY_PRODUCT_NAME_NOT_FOUND
					.getDescription(), productName));
		}

		log.info("IN method findByProductName - product: {} found by product name: {}", result, productName);
		return productMapper.toProductDTO(result);
	}

	@Override
	public Page<ProductDTO> findAllProducts(Pageable pageable) {
		Page<Product> productPage = productRepository.findAll(pageable);
		List<ProductDTO> productsDTOs = productMapper.toProductsDTOs(productPage.getContent());

		if (productPage.isEmpty()) {
			log.warn("IN method findAllProducts - no products found");
			throw new ProductException(ProductErrorType.PRODUCTS_NOT_FOUND.getDescription());
		}

		log.info("IN method findAllProducts - {} products found", productPage.getTotalElements());
		return new PageImpl<>(productsDTOs, pageable, productPage.getTotalElements());
	}

	@Override
	public void deleteById(int id) {
		Product result = productRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method deleteById - no product found by id: {}", id);
			throw new ProductException(String.format(ProductErrorType.PRODUCT_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method deleteById product with id: {} successfully deleted", id);
		productRepository.deleteById(id);
	}

	@Override
	public ProductDTO saveProductWithCategory(Product product) {
		Product productInDB = productRepository.findByProductName(product.getProductName()).orElse(new Product());

		productInDB.setProductName(product.getProductName());
		productInDB.getCategories()
				.addAll((product.getCategories()
						.stream()
						.map(c -> {
							Category productCategory = categoryRepository.findById(c.getId()).orElse(null);
							if (productCategory == null) {
								log.warn("IN method saveProductWithCategory - no category found by id: {}",
										c.getId());
								throw new CategoryException(String.format(CategoryErrorType.CATEGORY_BY_ID_NOT_FOUND
										.getDescription(), c.getId()));
							}
							productCategory.getProducts().add(productInDB);
							return productCategory;
						})).collect(Collectors.toList()));

		log.info("IN method saveProductWithCategory - product: {} with category: {} saved successfully",
				product.getProductName(), productInDB.getCategories());
		return productMapper.toProductDTO(productRepository.save(productInDB));
	}
}