package com.springboot.product_monitoring.exceptions.user;

public class UserException extends RuntimeException {

	public UserException(String message) {
		super(message);
	}
}