package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
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
import java.util.Date;
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
	public MessageResponse deleteById(int id) {
		Price result = priceRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method deleteById - no price found by id: {}", id);
			throw new PriceException(String.format(PriceErrorType.PRICE_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method deleteById price with id: {} successfully deleted", id);
		priceRepository.deleteById(id);
		return new MessageResponse("Price deleted successfully!");
	}

	@Override
	public PriceDTO savePriceWithProductNameAndStoreName(Price price) {
		Product product = productRepository.findByProductName(price.getProduct().getProductName()).orElse(null);

		if (product == null) {
			log.warn("IN method savePriceWithProductIdAndStoreId - no product found by id: {}",
					price.getProduct().getProductName());
			throw new ProductException(String.format(ProductErrorType.PRODUCT_BY_ID_NOT_FOUND
					.getDescription(), price.getProduct().getProductName()));
		}

		Store store = storeRepository.findByStoreName(price.getStore().getStoreName()).orElse(null);

		if (store == null) {
			log.warn("IN method savePriceWithProductIdAndStoreId - no store found by id: {}",
					price.getStore().getStoreName());
			throw new StoreException(String.format(StoreErrorType.STORE_BY_ID_NOT_FOUND
					.getDescription(), price.getStore().getStoreName()));
		}

		Price priceInDB = priceRepository.findById(price.getId()).orElse(new Price());
		Timestamp timestampNow = new Timestamp(System.currentTimeMillis());

		priceInDB.setDate(timestampNow);
		priceInDB.setUnitPrice(price.getUnitPrice());
		priceInDB.setProduct(product);
		priceInDB.setStore(store);

		log.info("IN method savePrice - price with product by id: {} and store by id: {} saved successfully",
				product.getId(), store.getId());
		return priceMapper.toPriceDTO(priceRepository.save(priceInDB));
	}

	@Override
	public Page<PriceDTO> findAllByDateBetweenAndProduct_ProductName(Date from, Date to, String productName,
	                                                                 Pageable pageable) {

		Page<Price> pricePage = priceRepository.findAllByDateBetweenAndProduct_ProductName(from, to, productName,
				pageable);
		List<PriceDTO> pricesDTOs = priceMapper.toPricesDTOs(pricePage.getContent());

		if (pricePage.isEmpty()) {
			log.warn("IN method findAllByDateBetweenAndProductName - no prices found by product name: {} " +
							"in the date range: {} - {}",
					productName, from, to);
			throw new PriceException(PriceErrorType.PRICES_NOT_FOUND.getDescription());
		}

		log.info("IN method findAllByDateBetweenAndProductName - {} prices found", pricePage.getTotalElements());
		return new PageImpl<>(pricesDTOs, pageable, pricePage.getTotalElements());
	}

	@Override
	public PriceDTO findPricesByProductNameAndStoreNameAndReturnGreatest(String productName, String firstStoreName,
	                                                              String secondStoreName) {

		Price onePrice = priceRepository.findFirstByProduct_ProductNameAndStore_StoreNameOrderByDateDesc(
				productName, firstStoreName);

		Price twoPrice = priceRepository.findFirstByProduct_ProductNameAndStore_StoreNameOrderByDateDesc(
				productName, secondStoreName);

		if (onePrice == null || twoPrice == null) {
			if (onePrice == null) {
				log.warn("IN method findPricesAndReturnGreatest - price by product name: {} and store name: {} " +
						"not found", productName, firstStoreName);
				throw new PriceException(String.format(PriceErrorType.PRICE_BY_PRODUCT_NAME__AND__STORE_NAME__NOT_FOUND
						.getDescription(), productName, firstStoreName));
			} else {
				log.warn("IN method findPricesAndReturnGreatest - price by product name: {} and store name: {} " +
						"not found", productName, secondStoreName);
				throw new PriceException(String.format(PriceErrorType.PRICE_BY_PRODUCT_NAME__AND__STORE_NAME__NOT_FOUND
						.getDescription(), productName, secondStoreName));
			}
		}

		Price greatestPrice = onePrice.getUnitPrice() > twoPrice.getUnitPrice() ? onePrice : twoPrice;
		log.info("IN method findPricesAndReturnGreatest - greatest price by product name: {} and store name: {} " +
				"was found", productName, greatestPrice.getStore().getStoreName());
		return priceMapper.toPriceDTO(greatestPrice);
	}
}