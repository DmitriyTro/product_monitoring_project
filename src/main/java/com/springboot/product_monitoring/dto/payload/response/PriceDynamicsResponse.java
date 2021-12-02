package com.springboot.product_monitoring.dto.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDynamicsResponse {

	private int id;
	private int unitPrice;
	private Timestamp date;

	public PriceDynamicsResponse(int id, int unitPrice, Timestamp date) {
		this.id = id;
		this.unitPrice = unitPrice;
		this.date = date;
	}
}