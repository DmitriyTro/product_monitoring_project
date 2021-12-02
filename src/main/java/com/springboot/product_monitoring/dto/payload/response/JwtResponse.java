package com.springboot.product_monitoring.dto.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JwtResponse {

	@JsonProperty("token")
	private String token;

	@JsonProperty("token type")
	private String type = "Bearer";

	@JsonProperty("user id")
	private int id;

	@JsonProperty("username")
	private String username;

	@JsonProperty("name")
	private String firstName;

	@JsonProperty("surname")
	private String lastName;

	@JsonProperty("e-mail")
	private String email;

	@JsonProperty("user roles")
	private List<String> roles;

	public JwtResponse(
			String accessToken,
			int id,
			String username,
			String firstName,
			String lastName,
			String email,
			List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roles = roles;
	}
}
