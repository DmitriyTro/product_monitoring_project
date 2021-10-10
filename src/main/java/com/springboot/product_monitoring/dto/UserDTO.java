package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.product_monitoring.entities.Role;
import lombok.Data;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {

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
	private Set<Role> roles;
}
