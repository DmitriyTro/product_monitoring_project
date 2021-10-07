package com.springboot.product_monitoring.exception.errors;

public enum StoresErrorType {

	STORE_BY_ID_NOT_FOUND("Store not found by id: %s"),
	STORE_BY_STORE_NAME_NOT_FOUND("Store not found by store name: %s"),
	STORE_ALREADY_EXISTS("Store by store name: %s already exists"),
	STORES_NOT_FOUND("Stores not found");

	private final String description;

	StoresErrorType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
