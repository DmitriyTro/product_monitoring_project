package com.springboot.product_monitoring.exceptions.errors;

public enum PriceErrorType {

	PRICE_BY_ID_NOT_FOUND("Price not found by id: %s"),
	PRICE_BY_PRODUCT_NAME__AND__STORE_NAME__NOT_FOUND("Price by product name: %s and store name: " +
			"%s not found"),
	PRICES_NOT_FOUND("Prices not found");

	private final String description;

	PriceErrorType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
