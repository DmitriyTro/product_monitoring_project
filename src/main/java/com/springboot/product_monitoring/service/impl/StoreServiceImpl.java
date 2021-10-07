package com.springboot.product_monitoring.service.impl;

import com.springboot.product_monitoring.entity.Store;
import com.springboot.product_monitoring.exception.errors.StoresErrorType;
import com.springboot.product_monitoring.exception.stores.StoresException;
import com.springboot.product_monitoring.repository.StoreRepository;
import com.springboot.product_monitoring.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;

	@Autowired
	public StoreServiceImpl(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

	@Override
	public Optional<Store> findStoreById(int id) {
		Store result = storeRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method findStoreById - no store found by id: {}", id);
			throw new StoresException(String.format(StoresErrorType.STORE_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method findStoreById - store found by id: {}", id);
		return Optional.of(result);
	}

	@Override
	public Optional<Store> findByStoreName(String storeName) {
		Store result = storeRepository.findByStoreName(storeName).orElse(null);

		if (result == null) {
			log.warn("IN method findByStoreName - no store found by store name: {}", storeName);
			throw new StoresException(String.format(StoresErrorType.STORE_BY_STORE_NAME_NOT_FOUND
					.getDescription(), storeName));
		}

		log.info("IN method findByStoreName - store: {} found by store name: {}", result, storeName);
		return Optional.of(result);
	}

	@Override
	public Page<Store> findAllStores(Pageable pageable) {
		Page<Store> result = storeRepository.findAll(pageable);

		if (result.isEmpty()) {
			log.warn("IN method findAllStores - no stores found");
			throw new StoresException(StoresErrorType.STORES_NOT_FOUND.getDescription());

		}

		log.info("IN method findAllStores - {} stores found", result.getTotalElements());
		return result;
	}

	@Override
	public void deleteById(int id) {
		Store result = storeRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method deleteById - no store found by id: {}", id);
			throw new StoresException(String.format(StoresErrorType.STORE_BY_ID_NOT_FOUND.getDescription(), id));

		}

		log.info("IN method deleteById store with id: {} successfully deleted", id);
		storeRepository.deleteById(id);
	}

	@Override
	public Store saveStore(Store store) {
		if (!storeRepository.existsByStoreName(store.getStoreName())) {
			log.info("IN method saveStore - store by store name: {} saved successfully", store.getStoreName());
			return storeRepository.save(store);
		} else {
			log.warn("IN method saveStore - store by store name: {} already exists", store.getStoreName());
			throw new StoresException(String.format(StoresErrorType.STORE_ALREADY_EXISTS.getDescription(),
					store.getStoreName()));
		}
	}
}