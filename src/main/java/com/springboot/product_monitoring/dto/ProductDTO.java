package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.product_monitoring.entities.Category;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDTO {

	@JsonProperty("product id")
	private int id;

	@JsonProperty("product name")
	private String productName;

	@JsonIgnoreProperties("products")
	@JsonProperty("product category")
	private List<Category> categories;
}
