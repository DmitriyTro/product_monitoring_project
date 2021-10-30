package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.product_monitoring.entities.Product;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoryDTO {

	private int id;
	private String categoryName;

	@JsonIgnoreProperties("categories")
	private List<Product> products;
}
