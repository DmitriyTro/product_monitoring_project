package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.entities.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface PriceService {

	PriceDTO findPriceById(int id);

	Page<PriceDTO> findAllPrices(Pageable pageable);

	void deleteById(int id);

	Page<PriceDTO> findAllByDateBetweenAndProduct_ProductName(Date from, Date to, String productName, Pageable pageable);

	PriceDTO savePriceWithProductIdAndStoreId(Price price, int productId, int storeId);
}
