package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDTO {

	private int id;
	private String roleName;
}
