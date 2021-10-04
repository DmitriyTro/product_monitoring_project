package com.springboot.product_monitoring.service;

import com.springboot.product_monitoring.entity.User;

import java.util.List;

public interface UserService {

	List<User> findAllUsers();

	User findByUsername(String username);

	User findById(int id);

	void deleteById(int id);
}
