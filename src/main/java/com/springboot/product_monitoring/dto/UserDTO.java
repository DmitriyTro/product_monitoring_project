package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.product_monitoring.entities.Role;
import lombok.Data;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {

	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;

	@JsonIgnoreProperties("users")
	private Set<Role> roles;
}
