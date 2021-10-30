package com.springboot.product_monitoring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.entities.Store;
import com.springboot.product_monitoring.services.PriceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.sql.Timestamp;
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
@ContextConfiguration(classes = PriceRestController.class)
class PriceRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PriceService priceService;

	@Autowired
	private ObjectMapper mapper;

	PriceDTO onePrice;

	PriceDTO twoPrice;

	@BeforeEach
	void setUp() {
		onePrice = new PriceDTO();
		onePrice.setId(1);
		onePrice.setUnitPrice(200);

		twoPrice = new PriceDTO();
		twoPrice.setId(2);
		twoPrice.setUnitPrice(150);
	}

	@AfterEach
	void tearDown() {
		onePrice = twoPrice = null;
	}

	@Test
	@WithMockUser(roles = {"USER", "ADMIN"})
	void findPriceById() throws Exception {
		when(priceService.findPriceById(anyInt())).thenReturn(onePrice);

		mockMvc.perform(get("/api/auth/prices/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.unitPrice", is(200)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"USER", "ADMIN"})
	void findAllPrices() throws Exception {
		PageRequest pageRequest =  PageRequest.of(0, 10);
		List<PriceDTO> prices = new ArrayList<>(Arrays.asList(onePrice, twoPrice));
		Page<PriceDTO> pricesPage = new PageImpl<>(prices, pageRequest, prices.size());

		when(priceService.findAllPrices(any(Pageable.class))).thenReturn(pricesPage);
		mockMvc.perform(get("/api/auth/prices/list")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteById() throws Exception {
		when(priceService.deleteById(twoPrice.getId())).thenReturn(new MessageResponse("Price deleted successfully!"));

		MvcResult requestResult = mockMvc.perform(delete("/api/auth/prices/delete/{priceId}",twoPrice.getId())
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		assertEquals(result.substring(12, 39), "Price deleted successfully!");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void savePriceWithProductIdAndStoreId() throws Exception {
		Product product = new Product();
		product.setId(2);

		Store store = new Store();
		store.setId(3);

		onePrice.setProduct(product);
		onePrice.setStore(store);

		when(priceService.savePriceWithProductIdAndStoreId(any(), anyInt(), anyInt())).thenReturn(onePrice);

		mockMvc.perform(put("/api/auth/prices/save")
						.param("productId", String.valueOf(product.getId()))
						.param("storeId", String.valueOf(store.getId()))
						.with(csrf())
						.content(mapper.writeValueAsString(onePrice))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.unitPrice", is(200)))
				.andExpect(jsonPath("$.product.id", is(2)))
				.andExpect(jsonPath("$.store.id", is(3)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(roles = {"USER", "ADMIN"})
	void findAllByDateBetweenAndProduct_ProductName() throws Exception {
		Timestamp timestamp = Timestamp.valueOf("2021-10-19 17:00:00.123456789");
		Product product = new Product();
		product.setId(1);
		product.setProductName("Fanta");

		onePrice.setDate(timestamp);
		onePrice.setProduct(product);

		twoPrice.setDate(timestamp);
		twoPrice.setProduct(product);

		PageRequest pageRequest =  PageRequest.of(0, 10);
		List<PriceDTO> prices = new ArrayList<>(Arrays.asList(onePrice, twoPrice));
		Page<PriceDTO> pricesPage = new PageImpl<>(prices, pageRequest, prices.size());

		when(priceService.findAllByDateBetweenAndProduct_ProductName(any(), any(), anyString(),
				any(Pageable.class))).thenReturn(pricesPage);

		mockMvc.perform(get("/api/auth/prices/list/date")
						.param("from", "2021-10-19")
						.param("to", "2021-10-21")
						.param("productName", "Fanta")
						.content(mapper.writeValueAsString(onePrice))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$.content[0].unitPrice", is(200)))
				.andExpect(jsonPath("$.content[0].date", is("2021-10-19T14:00:00.123+00:00")))
				.andExpect(jsonPath("$.content[0].product.id", is(1)))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(status().isOk());
	}
}