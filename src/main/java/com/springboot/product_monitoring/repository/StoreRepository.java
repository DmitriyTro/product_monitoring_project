package com.springboot.product_monitoring.repository;

import com.springboot.product_monitoring.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

	Optional<Store> findByStoreName(String storeName);

	Boolean existsByStoreName(String storeName);
}