package com.springboot.product_monitoring.service;

import com.springboot.product_monitoring.entity.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {

	Optional<User> findUserById(int id);

	Optional<User> findUserByUsername(String storeName);

	List<User> findAllUsers();

	User update(User user);

	void deleteById(int id);
}
