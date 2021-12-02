package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.payload.request.LoginRequest;
import com.springboot.product_monitoring.dto.payload.request.SignupRequest;
import com.springboot.product_monitoring.dto.payload.response.JwtResponse;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;

public interface AuthenticationService {

	JwtResponse authenticateUser(LoginRequest loginRequest);

	MessageResponse registerUser(SignupRequest signUpRequest);
}
