package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.payload.request.LoginRequest;
import com.springboot.product_monitoring.dto.payload.request.SignupRequest;
import com.springboot.product_monitoring.dto.payload.response.JwtResponse;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.security.service.UserDetailsImpl;
import com.springboot.product_monitoring.services.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationServiceTest {

	@MockBean
	private AuthenticationServiceImpl authenticationService;

	@Captor
	ArgumentCaptor<SignupRequest> argumentCaptor;

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
	@DisplayName("Should return authentication response")
	void authenticateUser() {

		LoginRequest loginRequest = new LoginRequest(userDetails.getUsername(), userDetails.getPassword());
		List<String> roles = new ArrayList<>(List.of(userDetails.getAuthorities().toString()));
		JwtResponse jwtResponse = new JwtResponse("testToken", 1, "testusername","testfn",
				"testln", "test@gmail.com", roles);

		when(authenticationService.authenticateUser(any())).thenReturn(jwtResponse);

		JwtResponse response = authenticationService.authenticateUser(loginRequest);

		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getUsername()).isEqualTo("testusername");
	}

	@Test
	@DisplayName("Should return register message")
	void registerUser() {

		Set<String> roles = new HashSet<>(Set.of(userDetails.getAuthorities().toString()));
		SignupRequest signUpRequest = new SignupRequest(userDetails.getUsername(), userDetails.getPassword(), userDetails.getFirstName(),
				userDetails.getLastName(), userDetails.getEmail(), roles);

		when(authenticationService.registerUser(any(SignupRequest.class)))
				.thenReturn(new MessageResponse("User registered successfully!"));

		MessageResponse actualRegisterMessage = authenticationService.registerUser(signUpRequest);
		verify(authenticationService, times(1)).registerUser(argumentCaptor.capture());

		assertThat(actualRegisterMessage.getMessage()).isEqualTo("User registered successfully!");
		assertThat(argumentCaptor.getValue().getUsername()).isEqualTo("testusername");
		assertThat(argumentCaptor.getValue().getFirstName()).isEqualTo("testuserfn");
	}
}