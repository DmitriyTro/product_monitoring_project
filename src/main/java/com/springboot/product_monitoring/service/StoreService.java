package com.springboot.product_monitoring.service;

import com.springboot.product_monitoring.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StoreService {

	Optional<Store> findStoreById(int id);

	Optional<Store> findByStoreName(String storeName);

	Page<Store> findAllStores(Pageable pageable);

	void deleteById(int id);

	Store saveStore(Store store);
}
