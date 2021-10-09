package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.product_monitoring.entities.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

	@NotBlank
	private int id;

	@NotBlank
	@Size(min = 5, max = 20)
	private String username;

	@Size(max = 25)
	private String firstName;

	@Size(max = 25)
	private String lastName;

	@Email
	@NotBlank
	@Size(max = 30)
	private String email;

	private Set<Role> roles;
}
