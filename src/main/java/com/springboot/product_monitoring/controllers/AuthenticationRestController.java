package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.dto.payload.request.LoginRequest;
import com.springboot.product_monitoring.dto.payload.request.SignupRequest;
import com.springboot.product_monitoring.dto.payload.response.JwtResponse;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.exceptions.role.RoleCustomExceptionHandler;
import com.springboot.product_monitoring.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RoleCustomExceptionHandler
@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationRestController {

	private final AuthenticationService authenticationService;

	@Autowired
	public AuthenticationRestController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
	}

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		return ResponseEntity.ok(new MessageResponse(authenticationService.registerUser(signUpRequest).getMessage()));
	}
}