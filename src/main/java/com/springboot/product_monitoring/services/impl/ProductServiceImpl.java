package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.dto.ProductDTO;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.exceptions.errors.ProductErrorType;
import com.springboot.product_monitoring.exceptions.product.ProductException;
import com.springboot.product_monitoring.mappers.ProductMapper;
import com.springboot.product_monitoring.repositories.ProductRepository;
import com.springboot.product_monitoring.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
		this.productRepository = productRepository;
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
	public ProductDTO saveProduct(Product product) {
		if (!productRepository.existsByProductName(product.getProductName())) {
			log.info("IN method saveProduct - product by product name: {} saved successfully", product.getProductName());
			return productMapper.toProductDTO(productRepository.save(product));
		} else {
			log.warn("IN method saveProduct - product by product name: {} already exists", product.getProductName());
			throw new ProductException(String.format(ProductErrorType.PRODUCT_ALREADY_EXISTS.getDescription(),
					product.getProductName()));
		}
	}
}