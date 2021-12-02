package com.springboot.product_monitoring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.product_monitoring.dto.StoreDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.services.StoreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = StoreRestController.class)
public class StoreRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StoreService storeService;

	@Autowired
	private ObjectMapper mapper;

	StoreDTO oneStore;

	StoreDTO twoStore;

	@BeforeEach()
	void setUp() {
		oneStore = new StoreDTO();
		oneStore.setId(1);
		oneStore.setStoreName("testing_1");

		twoStore = new StoreDTO();
		twoStore.setId(2);
		twoStore.setStoreName("testing_2");
	}

	@AfterEach
	void tearDown() {
		oneStore = twoStore = null;
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Should return store by id")
	void findStoreById() throws Exception {
		when(storeService.findStoreById(anyInt())).thenReturn(oneStore);

		mockMvc.perform(get("/api/auth/stores/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.storeName", is("testing_1")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Should return store by store name")
	void findByStoreName() throws Exception {
		when(storeService.findByStoreName(anyString())).thenReturn(oneStore);

		mockMvc.perform(get("/api/auth/stores/name/{storeName}", "testing_1")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.storeName", is("testing_1")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should return list of stores")
	void findAllStores() throws Exception {
		PageRequest pageRequest =  PageRequest.of(0, 10);
		List<StoreDTO> stores = new ArrayList<>(Arrays.asList(oneStore, twoStore));
		Page<StoreDTO> storesPage = new PageImpl<>(stores, pageRequest, stores.size());

		when(storeService.findAllStores(any(Pageable.class))).thenReturn(storesPage);
		mockMvc.perform(get("/api/auth/stores/list")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should delete store by id")
	void deleteById() throws Exception {
		when(storeService.deleteById(twoStore.getId())).thenReturn(new MessageResponse("Store deleted successfully!"));

		MvcResult requestResult = mockMvc.perform(delete("/api/auth/stores/delete/{storeId}",twoStore.getId())
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		assertEquals(result.substring(12, 39), "Store deleted successfully!");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should return saved store ib db")
	void saveStore() throws Exception {
		when(storeService.saveStore(any())).thenReturn(oneStore);

		mockMvc.perform(post("/api/auth/stores/save").with(csrf())
						.content(mapper.writeValueAsString(oneStore))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.storeName", is("testing_1")))
				.andExpect(status().isCreated());
	}
}