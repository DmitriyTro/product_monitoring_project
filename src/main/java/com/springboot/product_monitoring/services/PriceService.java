package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.dto.payload.response.PriceDynamicsResponse;
import com.springboot.product_monitoring.entities.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface PriceService {

	PriceDTO findPriceById(int id);

	Page<PriceDTO> findAllPrices(Pageable pageable);

	MessageResponse deleteById(int id);

	PriceDTO savePriceWithProductNameAndStoreName(Price price);

	List<PriceDynamicsResponse> findAllByDateBetweenAndProductIdAndStoreId(Date from, Date to, int productId, int storeId);

	PriceDTO findPricesByProductIdAndStoreIdAndReturnGreatest(int productId, int firstStoreId, int secondStoreId);

	Page<PriceDynamicsResponse> findPriceDynamicsByProductIdAndStoreId(int productId, int storeId, Pageable pageable);
}
