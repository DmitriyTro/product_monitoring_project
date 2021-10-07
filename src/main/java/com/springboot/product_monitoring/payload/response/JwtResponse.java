package com.springboot.product_monitoring.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private int id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> roles;

	public JwtResponse(String accessToken, int id, String username, String firstName, String lastName, String email, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roles = roles;
	}
}
