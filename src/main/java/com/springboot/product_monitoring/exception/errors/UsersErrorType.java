package com.springboot.product_monitoring.exception.errors;

public enum UsersErrorType {

	USER_BY_ID_NOT_FOUND("User not found by id: %s"),
	USER_BY_USERNAME_NOT_FOUND("User not found by username: %s"),
	USERS_NOT_FOUND("Users not found"),
	USER_NOT_UPDATED("User with id: %s not updated");

	private final String description;

	UsersErrorType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
