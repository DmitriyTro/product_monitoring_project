package com.springboot.product_monitoring.repositories;

import com.springboot.product_monitoring.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

	Store findByStoreName(String storeName);

	Boolean existsByStoreName(String storeName);
}