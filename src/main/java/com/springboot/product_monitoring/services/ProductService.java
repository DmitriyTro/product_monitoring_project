package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.ProductDTO;
import com.springboot.product_monitoring.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	ProductDTO findProductById(int id);

	ProductDTO findByProductName(String productName);

	Page<ProductDTO> findAllProducts(Pageable pageable);

	void deleteById(int id);

	ProductDTO saveProduct(Product product);

	ProductDTO saveProductWithCategory(Product product, String categoryName);
}
