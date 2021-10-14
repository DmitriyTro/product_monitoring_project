package com.springboot.product_monitoring.repositories;

import com.springboot.product_monitoring.entities.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface PriceRepository extends JpaRepository<Price, Integer> {

	Page<Price> findAllByDateBetweenAndProduct_ProductName(Date from, Date to, String productName, Pageable pageable);
}
