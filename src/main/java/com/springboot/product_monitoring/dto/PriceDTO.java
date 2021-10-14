package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.entities.Store;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class PriceDTO {

	@JsonProperty("price id")
	private int id;

	@JsonProperty("product price")
	private int unitPrice;

	@JsonProperty("date added")
	private Timestamp date;

	@JsonProperty("product name")
	@JsonIgnoreProperties("categories")
	private Product product;

	@JsonProperty("store name")
	private Store store;
}
