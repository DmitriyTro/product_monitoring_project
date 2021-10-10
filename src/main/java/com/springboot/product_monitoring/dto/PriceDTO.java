package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.entities.Store;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDTO {

	@JsonProperty("price id")
	private int id;

	@JsonProperty("product price")
	private int unitPrice;

	@JsonProperty("product name")
	private Product product;

	@JsonProperty("store name")
	private Store store;
}
