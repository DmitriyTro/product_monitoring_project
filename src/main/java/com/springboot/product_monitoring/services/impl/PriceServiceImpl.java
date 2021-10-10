package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.entities.Price;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.entities.Store;
import com.springboot.product_monitoring.exceptions.errors.PriceErrorType;
import com.springboot.product_monitoring.exceptions.errors.ProductErrorType;
import com.springboot.product_monitoring.exceptions.errors.StoreErrorType;
import com.springboot.product_monitoring.exceptions.price.PriceException;
import com.springboot.product_monitoring.exceptions.product.ProductException;
import com.springboot.product_monitoring.exceptions.store.StoreException;
import com.springboot.product_monitoring.mappers.PriceMapper;
import com.springboot.product_monitoring.repositories.PriceRepository;
import com.springboot.product_monitoring.repositories.ProductRepository;
import com.springboot.product_monitoring.repositories.StoreRepository;
import com.springboot.product_monitoring.services.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class PriceServiceImpl implements PriceService {

	private final PriceRepository priceRepository;
	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;
	private final PriceMapper priceMapper;

	@Autowired
	public PriceServiceImpl(PriceRepository priceRepository, ProductRepository productRepository, StoreRepository storeRepository, PriceMapper priceMapper) {
		this.priceRepository = priceRepository;
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
		this.priceMapper = priceMapper;
	}

	@Override
	public PriceDTO findPriceById(int id) {
		Price result = priceRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method findPriceById - no price found by id: {}", id);
			throw new PriceException(String.format(PriceErrorType.PRICE_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method findPriceById - price found by id: {}", id);
		return priceMapper.toPriceDTO(result);
	}

	@Override
	public Page<PriceDTO> findAllPrices(Pageable pageable) {
		Page<Price> pricePage = priceRepository.findAll(pageable);
		List<PriceDTO> pricesDTOs = priceMapper.toPricesDTOs(pricePage.getContent());

		if (pricePage.isEmpty()) {
			log.warn("IN method findAllPrices - no prices found");
			throw new PriceException(PriceErrorType.PRICES_NOT_FOUND.getDescription());
		}

		log.info("IN method findAllPrices - {} prices found", pricePage.getTotalElements());
		return new PageImpl<>(pricesDTOs, pageable, pricePage.getTotalElements());
	}

	@Override
	public void deleteById(int id) {
		Price result = priceRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method deleteById - no price found by id: {}", id);
			throw new PriceException(String.format(PriceErrorType.PRICE_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method deleteById price with id: {} successfully deleted", id);
		priceRepository.deleteById(id);
	}

	@Override
	public PriceDTO savePriceWithProductIdAndStoreId(Price price, int productId, int storeId) {
		Timestamp timestampNow = new Timestamp(System.currentTimeMillis());
		Product product = productRepository.findById(productId).orElse(null);
		if (product == null) {
			log.warn("IN method savePrice - no product found by id: {}", productId);
			throw new ProductException(String.format(ProductErrorType.PRODUCT_BY_ID_NOT_FOUND.getDescription(), productId));
		}

		Store store = storeRepository.findById(storeId).orElse(null);
		if (store == null) {
			log.warn("IN method savePrice - no store found by id: {}", storeId);
			throw new StoreException(String.format(StoreErrorType.STORE_BY_ID_NOT_FOUND.getDescription(), storeId));
		}

		price.setDate(timestampNow);
		price.setProduct(product);
		price.setStore(store);

		log.info("IN method savePrice - price with product by id: {} and store by id: {} saved successfully",
				productId, storeId);
		return priceMapper.toPriceDTO(priceRepository.save(price));
	}
}