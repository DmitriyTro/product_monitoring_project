package com.springboot.product_monitoring.dto.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

	@NotBlank
	@Size(min = 5, max = 20)
	private String username;

	@NotBlank
	@Size(min = 6, max = 25)
	private String password;

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
