package com.springboot.product_monitoring.repositories;

import com.springboot.product_monitoring.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Integer> {

	Boolean existsById(int id);
}
