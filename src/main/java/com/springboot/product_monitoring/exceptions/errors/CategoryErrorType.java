package com.springboot.product_monitoring.exceptions.errors;

public enum CategoryErrorType {

	CATEGORY_BY_ID_NOT_FOUND("Category not found by id: %s"),
	CATEGORY_BY_CATEGORY_NAME_NOT_FOUND("Category not found by category name: %s"),
	CATEGORY_ALREADY_EXISTS("Category by category name: %s already exists"),
	CATEGORIES_NOT_FOUND("Categories not found");

	private final String description;

	CategoryErrorType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
