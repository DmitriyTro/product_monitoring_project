package com.springboot.product_monitoring.dto.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {

	@JsonProperty("username")
	@NotBlank
	@Size(min = 5, max = 20)
	private String username;

	@JsonProperty("password")
	@NotBlank
	@Size(min = 6, max = 25)
	private String password;

	@JsonProperty("name")
	@Size(max = 25)
	private String firstName;

	@JsonProperty("surname")
	@Size(max = 25)
	private String lastName;

	@JsonProperty("e-mail")
	@Email
	@NotBlank
	@Size(max = 30)
	private String email;

	@JsonProperty("user roles")
	private Set<String> role;
}
