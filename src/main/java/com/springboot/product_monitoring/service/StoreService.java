package com.springboot.product_monitoring.service;

import com.springboot.product_monitoring.entity.Store;

import java.util.List;
import java.util.Optional;

public interface StoreService {

	Optional<Store> findStoreById(int id);

	Optional<Store> findByStoreName(String storeName);

	List<Store> findAllStores();

	void deleteById(int id);

	Store saveStore(Store store);
}
