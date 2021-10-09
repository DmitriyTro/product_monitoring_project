package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.product_monitoring.entities.Category;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

	@NotBlank
	private int id;

	@NotBlank
	@Size(max = 30)
	private String productName;

	private List<Category> categories;
}
