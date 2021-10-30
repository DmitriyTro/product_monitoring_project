package com.springboot.product_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.entities.Store;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class PriceDTO {

	private int id;
	private int unitPrice;
	private Timestamp date;

	@JsonIgnoreProperties("categories")
	private Product product;

	@JsonIgnoreProperties("products")
	private Store store;
}
