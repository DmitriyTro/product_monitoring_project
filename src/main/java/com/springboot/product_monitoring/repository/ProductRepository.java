package com.springboot.product_monitoring.repository;

import com.springboot.product_monitoring.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	Optional<Product> findByProductName(String productName);

	Boolean existsByProductName(String productName);
}
