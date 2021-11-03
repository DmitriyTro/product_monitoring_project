package com.springboot.product_monitoring.exceptions.errors;

public enum PriceErrorType {

	PRICE_BY_ID_NOT_FOUND("Price not found by id: %s"),
	PRICE_BY_PRODUCT_ID__AND__STORE_ID__NOT_FOUND("Price by product id: %s and store id: " +
			"%s not found"),
	PRICES_BY_PRODUCT_ID__AND__STORE_ID__NOT_FOUND("Prices by product id: %s and store id: " +
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
