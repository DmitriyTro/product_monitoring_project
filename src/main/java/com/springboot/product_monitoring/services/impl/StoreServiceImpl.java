package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.dto.StoreDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Store;
import com.springboot.product_monitoring.exceptions.errors.StoreErrorType;
import com.springboot.product_monitoring.exceptions.store.StoreException;
import com.springboot.product_monitoring.mappers.StoreMapper;
import com.springboot.product_monitoring.repositories.StoreRepository;
import com.springboot.product_monitoring.services.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;
	private final StoreMapper storeMapper;

	@Autowired
	public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper) {
		this.storeRepository = storeRepository;
		this.storeMapper = storeMapper;
	}

	@Override
	public StoreDTO findStoreById(int id) {
		Optional<Store> result = storeRepository.findById(id);

		if (result.isEmpty()) {
			log.warn("IN method findStoreById - no store found by id: {}", id);
			throw new StoreException(String.format(StoreErrorType.STORE_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method findStoreById - store found by id: {}", id);
		return storeMapper.toStoreDTO(result.get());
	}

	@Override
	public StoreDTO findByStoreName(String storeName) {
		Store result = storeRepository.findByStoreName(storeName);

		if (result == null) {
			log.warn("IN method findByStoreName - no store found by store name: {}", storeName);
			throw new StoreException(String.format(StoreErrorType.STORE_BY_STORE_NAME_NOT_FOUND
					.getDescription(), storeName));
		}

		log.info("IN method findByStoreName - store: {} found by store name: {}", result, storeName);
		return storeMapper.toStoreDTO(result);
	}

	@Override
	public Page<StoreDTO> findAllStores(Pageable pageable) {
		Page<Store> storePage = storeRepository.findAll(pageable);
		List<StoreDTO> storesDTOs = storeMapper.toStoresDTOs(storePage.getContent());

		if (storePage.isEmpty()) {
			log.warn("IN method findAllStores - no stores found");
			throw new StoreException(StoreErrorType.STORES_NOT_FOUND.getDescription());
		}

		log.info("IN method findAllStores - {} stores found", storePage.getTotalElements());
		return new PageImpl<>(storesDTOs, pageable, storePage.getTotalElements());
	}

	@Override
	public MessageResponse deleteById(int id) {
		Optional<Store> result = storeRepository.findById(id);

		if (result.isEmpty()) {
			log.warn("IN method deleteById - no store found by id: {}", id);
			throw new StoreException(String.format(StoreErrorType.STORE_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method deleteById store with id: {} successfully deleted", id);
		storeRepository.deleteById(id);
		return new MessageResponse("Store deleted successfully!");
	}

	@Override
	public StoreDTO saveStore(Store store) {
		if (!storeRepository.existsByStoreName(store.getStoreName())) {
			log.info("IN method saveStore - store by store name: {} saved successfully", store.getStoreName());
			return storeMapper.toStoreDTO(storeRepository.save(store));
		} else {
			log.warn("IN method saveStore - store by store name: {} already exists", store.getStoreName());
			throw new StoreException(String.format(StoreErrorType.STORE_ALREADY_EXISTS.getDescription(),
					store.getStoreName()));
		}
	}
}