package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.dto.payload.response.PriceDynamicsResponse;
import com.springboot.product_monitoring.entities.Price;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.entities.Store;
import com.springboot.product_monitoring.mappers.PriceDynamicsResponseMapper;
import com.springboot.product_monitoring.mappers.PriceMapper;
import com.springboot.product_monitoring.repositories.PriceRepository;
import com.springboot.product_monitoring.repositories.ProductRepository;
import com.springboot.product_monitoring.repositories.StoreRepository;
import com.springboot.product_monitoring.services.impl.PriceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PriceServiceTest {

	@MockBean
	PriceRepository priceRepository;

	@MockBean
	ProductRepository productRepository;

	@MockBean
	StoreRepository storeRepository;

	@MockBean
	PriceMapper priceMapper;

	@MockBean
	PriceDynamicsResponseMapper priceDynamicsResponseMapper;

	@Captor
	ArgumentCaptor<Price> argumentCaptor;

	@MockBean
	PriceServiceImpl priceService;

	Price price;

	Product product;

	Store store;

	PriceDTO priceDTO;

	List<Price> prices;

	@BeforeEach
	void setUp() {
		priceService = new PriceServiceImpl(priceRepository,
				productRepository,
				storeRepository,
				priceMapper,
				priceDynamicsResponseMapper);

		Timestamp timestamp = Timestamp.valueOf("2021-10-19 17:00:00.123456789");

		product = new Product();
		product.setId(1);
		product.setProductName("testproduct");

		store = new Store();
		store.setId(2);
		store.setStoreName("teststore");

		price = new Price();
		price.setId(1);
		price.setDate(timestamp);
		price.setUnitPrice(200);
		price.setProduct(product);
		price.setStore(store);

		priceDTO = new PriceDTO();
		priceDTO.setId(1);
		priceDTO.setDate(timestamp);
		priceDTO.setUnitPrice(200);
		priceDTO.setProduct(product);
		priceDTO.setStore(store);
	}

	@AfterEach
	void tearDown() {

		price = null;
		product = null;
		store = null;
		priceDTO = null;
	}

	@Test
	@DisplayName("Should return price by id")
	void findPriceById() {

		when(priceRepository.findById(1)).thenReturn(Optional.ofNullable(price));
		when(priceMapper.toPriceDTO(any(Price.class))).thenReturn(priceDTO);

		PriceDTO actualPriceDTO = priceService.findPriceById(1);

		assertThat(actualPriceDTO.getId()).isEqualTo(1);
		assertThat(actualPriceDTO.getUnitPrice()).isEqualTo(200);
		assertThat(actualPriceDTO.getProduct().getId()).isEqualTo(1);
		assertThat(actualPriceDTO.getStore().getId()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should return list of prices")
	void findAllPrices() {

		PageRequest pageRequest = PageRequest.of(0, 10);
		prices = new ArrayList<>(List.of(price, price));
		Page<Price> pricePage = new PageImpl<>(prices, pageRequest, prices.size());

		when(priceRepository.findAll(pageRequest)).thenReturn(pricePage);
		Page<PriceDTO> actualPricePage = priceService.findAllPrices(pageRequest);

		assertThat(pricePage.getContent().get(0).getId()).isEqualTo(1);
		assertThat(pricePage.getContent().size()).isEqualTo(2);
		assertThat(actualPricePage.getPageable().getPageSize()).isEqualTo(10);
		assertThat(actualPricePage.getTotalElements()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should delete price by id")
	void deleteById() {

		when(priceRepository.findById(1)).thenReturn(Optional.of(price));

		MessageResponse actualPrice = priceService.deleteById(price.getId());

		assertEquals("Price deleted successfully!", actualPrice.getMessage());
	}

	@Test
	@DisplayName("Should return list of prices by date between")
	void findAllByDateBetweenAndProduct_Id() throws ParseException {

		Timestamp timestamp = Timestamp.valueOf("2021-10-19 17:00:00.123456789");
		Date from = Date.valueOf("2021-10-18");
		Date to = Date.valueOf("2021-10-20");

		PriceDynamicsResponse onePriceDR = new PriceDynamicsResponse(1, 200, timestamp);
		PriceDynamicsResponse secondPriceDR = new PriceDynamicsResponse(1, 200,timestamp);
		List<PriceDynamicsResponse> mappedPrices = new ArrayList<>(List.of(onePriceDR, secondPriceDR));

		prices = new ArrayList<>(List.of(price, price));

		when(priceRepository.findPricesByDateBetweenAndProduct_IdAndStore_Id(any(), any(), anyInt(),
				anyInt())).thenReturn(prices);

		when(priceDynamicsResponseMapper.toPricesDynamicResponse(prices)).thenReturn(mappedPrices);

		List<PriceDynamicsResponse> actualPrices = priceService.findAllByDateBetweenAndProductIdAndStoreId
				(from, to, 1, 2);

		assertThat(actualPrices.get(0).getId()).isEqualTo(1);
		assertThat(actualPrices.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should save price with product and store")
	void savePriceWithProductNameAndStoreName() {

		when(priceRepository.findById(1)).thenReturn(Optional.of(price));
		when(productRepository.findByProductName("testproduct")).thenReturn(Optional.ofNullable(product));
		when(storeRepository.findByStoreName("teststore")).thenReturn(Optional.of(store));

		priceService.savePriceWithProductNameAndStoreName(price);

		verify(priceRepository, times(1)).save(argumentCaptor.capture());

		assertThat(argumentCaptor.getValue().getId()).isEqualTo(1);
		assertThat(argumentCaptor.getValue().getUnitPrice()).isEqualTo(200);
		assertThat(argumentCaptor.getValue().getProduct().getId()).isEqualTo(1);
		assertThat(argumentCaptor.getValue().getStore().getId()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should return price with greatest unitPrice")
	void findPricesByProductIdAndStoreIdAndReturnGreatest() {

		Store testStore = new Store();
		testStore.setId(3);

		Price secondTestPrice = new Price();
		secondTestPrice.setId(2);
		secondTestPrice.setUnitPrice(100);
		secondTestPrice.setProduct(product);
		secondTestPrice.setStore(testStore);

		when(priceRepository.findFirstByProduct_IdAndStore_IdOrderByDateDesc(price.getProduct().getId(),
				price.getStore().getId())).thenReturn(price);

		when(priceRepository.findFirstByProduct_IdAndStore_IdOrderByDateDesc(secondTestPrice.getProduct().getId(),
				secondTestPrice.getStore().getId())).thenReturn(secondTestPrice);
		when(priceMapper.toPriceDTO(any(Price.class))).thenReturn(priceDTO);

		PriceDTO actualPrice = priceService.findPricesByProductIdAndStoreIdAndReturnGreatest(
				1, 2, 3);

		assertThat(actualPrice.getId()).isEqualTo(1);
		assertThat(actualPrice.getUnitPrice()).isEqualTo(200);
		assertThat(actualPrice.getProduct().getId()).isEqualTo(1);
		assertThat(actualPrice.getStore().getId()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should return page of priceResponse")
	void findPriceDynamicsByProductIdAndStoreId() {

		PageRequest pageRequest = PageRequest.of(0, 10);
		prices = new ArrayList<>(List.of(price, price));
		Page<Price> pricePage = new PageImpl<>(prices, pageRequest, prices.size());

		when(priceRepository.findPricesByProduct_IdAndStore_Id(product.getId(), store.getId(),
				pageRequest)).thenReturn(pricePage);

		Page<PriceDynamicsResponse> actualPricePage = priceService.findPriceDynamicsByProductIdAndStoreId(
				1, 2, pageRequest);

		assertThat(pricePage.getContent().get(0).getId()).isEqualTo(1);
		assertThat(pricePage.getContent().size()).isEqualTo(2);
		assertThat(actualPricePage.getPageable().getPageSize()).isEqualTo(10);
		assertThat(actualPricePage.getTotalElements()).isEqualTo(2);
	}
}