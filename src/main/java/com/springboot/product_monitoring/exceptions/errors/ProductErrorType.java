package com.springboot.product_monitoring.exceptions.errors;

public enum ProductErrorType {

	PRODUCT_BY_ID_NOT_FOUND("Product not found by id: %s"),
	PRODUCT_BY_PRODUCT_NAME_NOT_FOUND("Product not found by product name: %s"),
	PRODUCT_ALREADY_EXISTS("Product by product name: %s already exists"),
	PRODUCTS_NOT_FOUND("Products not found");

	private final String description;

	ProductErrorType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
