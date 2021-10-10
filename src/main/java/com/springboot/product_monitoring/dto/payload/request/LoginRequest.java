package com.springboot.product_monitoring.dto.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

	@JsonProperty("username")
	@NotBlank
	private String username;

	@JsonProperty("password")
	@NotBlank
	private String password;
}
