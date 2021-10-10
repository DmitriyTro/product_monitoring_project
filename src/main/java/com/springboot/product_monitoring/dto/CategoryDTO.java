package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.product_monitoring.entities.Product;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoryDTO {

	@JsonProperty("category id")
	private int id;

	@JsonProperty("products category")
	private String categoryName;

	@JsonProperty("description")
	private String description;

	@JsonProperty("products in category")
	private List<Product> products;
}
