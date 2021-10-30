package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.StoreDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {

	StoreDTO findStoreById(int id);

	StoreDTO findByStoreName(String storeName);

	Page<StoreDTO> findAllStores(Pageable pageable);

	MessageResponse deleteById(int id);

	StoreDTO saveStore(Store store);
}
