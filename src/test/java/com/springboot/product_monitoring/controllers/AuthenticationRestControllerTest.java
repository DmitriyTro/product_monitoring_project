package com.springboot.product_monitoring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.product_monitoring.dto.payload.request.LoginRequest;
import com.springboot.product_monitoring.dto.payload.request.SignupRequest;
import com.springboot.product_monitoring.dto.payload.response.JwtResponse;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.security.service.UserDetailsImpl;
import com.springboot.product_monitoring.services.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = AuthenticationRestController.class)
class AuthenticationRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationService authenticationService;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private UserDetailsImpl userDetails;

	@BeforeEach
	void setUp() {

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
		userDetails = new UserDetailsImpl(1, "testuserfn", "testuserln",
				"testusername", "secret", "testuser@gmail.com", authorities);
	}

	@AfterEach
	void tearDown() {

		userDetails = null;
	}

	@Test
	void authenticateUser() throws Exception {
		LoginRequest loginRequest = new LoginRequest(userDetails.getUsername(), userDetails.getPassword());
		List<String> roles = new ArrayList<>(List.of(userDetails.getAuthorities().toString()));
		JwtResponse jwtResponse = new JwtResponse("testToken", 1, "testusername","testfn",
				"testln", "test@gmail.com", roles);

		when(authenticationService.authenticateUser(any(LoginRequest.class))).thenReturn(jwtResponse);

		mockMvc.perform(post("/api/auth/signin").with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(loginRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.username", is("testusername")))
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(status().isOk());
	}

	@Test
	void registerUser() throws Exception {
		Set<String> roles = new HashSet<>(Set.of(userDetails.getAuthorities().toString()));
		SignupRequest signUpRequest = new SignupRequest(userDetails.getUsername(), userDetails.getPassword(), userDetails.getFirstName(),
				userDetails.getLastName(), userDetails.getEmail(), roles);

		when(authenticationService.registerUser(any(SignupRequest.class)))
				.thenReturn(new MessageResponse("User registered successfully!"));

		mockMvc.perform(post("/api/auth/signup").with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(signUpRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.message", is("User registered successfully!")))
				.andExpect(status().isOk());
	}
}