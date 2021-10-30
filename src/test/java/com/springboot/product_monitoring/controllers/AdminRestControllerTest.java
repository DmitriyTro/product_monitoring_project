package com.springboot.product_monitoring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.services.AdminService;
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
@ContextConfiguration(classes = AdminRestController.class)
class AdminRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AdminService adminService;

	@Autowired
	private ObjectMapper mapper;

	UserDTO oneUser;

	UserDTO twoUser;

	@BeforeEach
	void setUp() {
		oneUser = new UserDTO();
		oneUser.setId(1);
		oneUser.setUsername("testing_1");
		oneUser.setPassword("testing");
		oneUser.setEmail("user@gmail.com");

		twoUser = new UserDTO();
		twoUser.setId(2);
		twoUser.setUsername("testing_2");
	}

	@AfterEach
	void tearDown() {
		oneUser = twoUser = null;
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void findUserById() throws Exception {
		when(adminService.findUserById(anyInt())).thenReturn(oneUser);

		mockMvc.perform(get("/api/admin/users/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.username", is("testing_1")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void findByUsername() throws Exception {
		when(adminService.findUserByUsername(anyString())).thenReturn(oneUser);

		mockMvc.perform(get("/api/admin/users/name/{userName}", "testing_1")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.username", is("testing_1")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void findAllUsers() throws Exception {
		PageRequest pageRequest =  PageRequest.of(0, 10);
		List<UserDTO> users = new ArrayList<>(Arrays.asList(oneUser, twoUser));
		Page<UserDTO> usersPage = new PageImpl<>(users, pageRequest, users.size());

		when(adminService.findAllUsers(any(Pageable.class))).thenReturn(usersPage);
		mockMvc.perform(get("/api/admin/users/list")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void update() throws Exception {
		when(adminService.update(any())).thenReturn(oneUser);

		mockMvc.perform(put("/api/admin/users/update").with(csrf())
						.content(mapper.writeValueAsString(oneUser))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.username", is("testing_1")))
				.andExpect(jsonPath("$.email", is("user@gmail.com")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteById() throws Exception {
		when(adminService.deleteById(twoUser.getId())).thenReturn(new MessageResponse("User deleted successfully!"));

		MvcResult requestResult = mockMvc.perform(delete("/api/admin/users/delete/{userId}",twoUser.getId())
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		assertEquals(result.substring(12, 38), "User deleted successfully!");
	}
}