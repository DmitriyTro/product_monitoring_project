package com.springboot.product_monitoring.exceptions.errors;

public enum RoleErrorType {

	ROLE_NOT_FOUND("Role not found"),
	ROLE_BY_ID_NOT_FOUND("Role not found by id: %s");

	private final String description;

	RoleErrorType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
