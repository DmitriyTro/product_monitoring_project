package com.springboot.product_monitoring.exceptions.errors;

public enum PriceErrorType {

	PRICE_BY_ID_NOT_FOUND("Price not found by id: %s"),
	PRICE_ALREADY_EXISTS("Price: %s already exists"),
	PRICES_NOT_FOUND("Prices not found");

	private final String description;

	PriceErrorType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
