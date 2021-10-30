package com.springboot.product_monitoring.dto.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {

	@NotBlank
	@Size(min = 5, max = 20)
	private String username;

	@NotBlank
	@Size(min = 6, max = 25)
	private String password;

	@Size(max = 25)
	private String firstName;

	@Size(max = 25)
	private String lastName;

	@Email
	@NotBlank
	@Size(max = 30)
	private String email;

	private Set<String> role;

	public SignupRequest(String username, String password, String firstName, String lastName, String email, Set<String> role) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
	}
}
