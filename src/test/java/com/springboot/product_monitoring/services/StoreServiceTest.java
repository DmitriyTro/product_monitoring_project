package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.StoreDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Store;
import com.springboot.product_monitoring.mappers.StoreMapper;
import com.springboot.product_monitoring.repositories.StoreRepository;
import com.springboot.product_monitoring.services.impl.StoreServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class StoreServiceTest {

	@MockBean
	StoreRepository storeRepository;

	@MockBean
	StoreMapper storeMapper;

	@Captor
	ArgumentCaptor<Store> argumentCaptor;

	@MockBean
	StoreServiceImpl storeService;

	Store store;

	StoreDTO storeDTO;

	List<Store> stores;

	@BeforeEach
	void setUp() {
		storeService = new StoreServiceImpl(storeRepository, storeMapper);

		store = new Store();
		store.setId(1);
		store.setStoreName("teststore");

		storeDTO = new StoreDTO();
		storeDTO.setId(1);
		storeDTO.setStoreName("teststore");
	}

	@AfterEach
	void tearDown() {

		store = null;
		storeDTO = null;
	}

	@Test
	@DisplayName("Should return store by id")
	void findStoreById() {

		when(storeRepository.findById(1)).thenReturn(Optional.ofNullable(store));
		when(storeMapper.toStoreDTO(any(Store.class))).thenReturn(storeDTO);

		StoreDTO actualStoreDTO = storeService.findStoreById(1);

		assertThat(actualStoreDTO.getId()).isEqualTo(1);
		assertThat(actualStoreDTO.getStoreName()).isEqualTo("teststore");
	}

	@Test
	@DisplayName("Should return store by store name")
	void findByStoreName() {

		when(storeRepository.findByStoreName("teststore")).thenReturn(Optional.ofNullable(store));
		when(storeMapper.toStoreDTO(any(Store.class))).thenReturn(storeDTO);

		StoreDTO actualStoreDTO = storeService.findByStoreName("teststore");

		assertThat(actualStoreDTO.getId()).isEqualTo(1);
		assertThat(actualStoreDTO.getStoreName()).isEqualTo("teststore");
	}

	@Test
	@DisplayName("Should return list of stores")
	void findAllStores() {

		PageRequest pageRequest =  PageRequest.of(0, 10);
		stores = new ArrayList<>(List.of(store, store));
		Page<Store> storePage = new PageImpl<>(stores, pageRequest, stores.size());

		when(storeRepository.findAll(pageRequest)).thenReturn(storePage);
		Page<StoreDTO> actualStorePage = storeService.findAllStores(pageRequest);

		assertThat(storePage.getContent().get(0).getId()).isEqualTo(1);
		assertThat(storePage.getContent().size()).isEqualTo(2);
		assertThat(actualStorePage.getPageable().getPageSize()).isEqualTo(10);
		assertThat(actualStorePage.getTotalElements()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should delete store by id")
	void deleteById() {

		when(storeRepository.findById(1)).thenReturn(Optional.of(store));

		MessageResponse actualStore = storeService.deleteById(store.getId());

		assertEquals("Store deleted successfully!", actualStore.getMessage());
	}

	@Test
	@DisplayName("Should save store")
	void saveStore() {

		when(storeRepository.findByStoreName("teststore")).thenReturn(Optional.of(store));

		storeService.saveStore(store);
		verify(storeRepository, times(1)).save(argumentCaptor.capture());

		assertThat(argumentCaptor.getValue().getId()).isEqualTo(1);
		assertThat(argumentCaptor.getValue().getStoreName()).isEqualTo("teststore");
	}
}