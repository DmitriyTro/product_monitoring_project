package com.springboot.product_monitoring.service;

import com.springboot.product_monitoring.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

	Optional<Product> findProductById(int id);

	Optional<Product> findByProductName(String productName);

	Page<Product> findAllProducts(Pageable pageable);

	void deleteById(int id);

	Product saveProduct(Product product);
}
