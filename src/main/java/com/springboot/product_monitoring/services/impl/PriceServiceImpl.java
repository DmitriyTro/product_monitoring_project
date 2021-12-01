package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.dto.payload.response.PriceDynamicsResponse;
import com.springboot.product_monitoring.entities.Price;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.entities.Store;
import com.springboot.product_monitoring.exceptions.errors.PriceErrorType;
import com.springboot.product_monitoring.exceptions.errors.ProductErrorType;
import com.springboot.product_monitoring.exceptions.errors.StoreErrorType;
import com.springboot.product_monitoring.exceptions.price.PriceException;
import com.springboot.product_monitoring.exceptions.product.ProductException;
import com.springboot.product_monitoring.exceptions.store.StoreException;
import com.springboot.product_monitoring.mappers.PriceDynamicsResponseMapper;
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
	private final PriceDynamicsResponseMapper priceDynamicsResponseMapper;

	@Autowired
	public PriceServiceImpl(PriceRepository priceRepository,
	                        ProductRepository productRepository,
	                        StoreRepository storeRepository,
	                        PriceMapper priceMapper,
	                        PriceDynamicsResponseMapper priceDynamicsResponseMapper) {
		this.priceRepository = priceRepository;
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
		this.priceMapper = priceMapper;
		this.priceDynamicsResponseMapper = priceDynamicsResponseMapper;
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
		Product product = productRepository.findById(price.getProduct().getId()).orElse(null);

		if (product == null) {
			log.warn("IN method savePriceWithProductIdAndStoreId - no product found by id: {}",
					price.getProduct().getId());
			throw new ProductException(String.format(ProductErrorType.PRODUCT_BY_ID_NOT_FOUND
					.getDescription(), price.getProduct().getId()));
		}

		Store store = storeRepository.findById(price.getStore().getId()).orElse(null);

		if (store == null) {
			log.warn("IN method savePriceWithProductIdAndStoreId - no store found by id: {}",
					price.getStore().getId());
			throw new StoreException(String.format(StoreErrorType.STORE_BY_ID_NOT_FOUND
					.getDescription(), price.getStore().getId()));
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
	public List<PriceDynamicsResponse> findAllByDateBetweenAndProductIdAndStoreId(
			Date from, Date to, int productId, int storeId) {

		List<Price> prices = priceRepository.findPricesByDateBetweenAndProduct_IdAndStore_Id(
				from, to, productId, storeId);

		List<PriceDynamicsResponse> pricesDynamics = priceDynamicsResponseMapper.toPricesDynamicResponse(prices);

		log.info("IN method findAllByDateBetweenAndProductName - {} prices found", prices.size());
		return pricesDynamics;
	}

	@Override
	public PriceDTO findPricesByProductIdAndStoreIdAndReturnGreatest(int productId, int firstStoreId,
	                                                                 int secondStoreId) {

		Price onePrice = priceRepository.findFirstByProduct_IdAndStore_IdOrderByDateDesc(productId, firstStoreId);

		Price twoPrice = priceRepository.findFirstByProduct_IdAndStore_IdOrderByDateDesc(productId, secondStoreId);

		if (onePrice == null || twoPrice == null) {
			if (onePrice == null) {
				log.warn("IN method findPricesAndReturnGreatest - price by product id: {} and store id: {} " +
						"not found", productId, firstStoreId);
				throw new PriceException(String.format(PriceErrorType.PRICE_BY_PRODUCT_ID__AND__STORE_ID__NOT_FOUND
						.getDescription(), productId, firstStoreId));
			} else {
				log.warn("IN method findPricesAndReturnGreatest - price by product id: {} and store id: {} " +
						"not found", productId, secondStoreId);
				throw new PriceException(String.format(PriceErrorType.PRICE_BY_PRODUCT_ID__AND__STORE_ID__NOT_FOUND
						.getDescription(), productId, secondStoreId));
			}
		}

		Price greatestPrice = onePrice.getUnitPrice() > twoPrice.getUnitPrice() ? onePrice : twoPrice;

		log.info("IN method findPricesAndReturnGreatest - greatest price by product id: {} and store id: {} " +
				"was found", productId, greatestPrice.getStore().getId());
		return priceMapper.toPriceDTO(greatestPrice);
	}

	@Override
	public Page<PriceDynamicsResponse> findPriceDynamicsByProductIdAndStoreId(int productId, int storeId,
	                                                                          Pageable pageable) {

		Page<Price> pricePage = priceRepository.findPricesByProduct_IdAndStore_Id(productId, storeId,
				pageable);

		if (pricePage.isEmpty()) {
			log.warn("IN method findPriceDynamics - prices by product id: {} and store id: {} " +
					"not found", productId, storeId);
			throw new PriceException(String.format(PriceErrorType.PRICES_BY_PRODUCT_ID__AND__STORE_ID__NOT_FOUND
					.getDescription(), productId, storeId));
		}

		List<PriceDynamicsResponse> pricesDynamics = priceDynamicsResponseMapper.toPricesDynamicResponse(pricePage.getContent());

		log.info("IN method findPriceDynamics - list of price dynamics return successfully, total elements: {}",
				pricePage.getTotalElements());
		return new PageImpl<>(pricesDynamics, pageable, pricePage.getTotalElements());
	}
}