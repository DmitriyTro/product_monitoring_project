package com.springboot.product_monitoring.service;

import com.springboot.product_monitoring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminService {

	Optional<User> findUserById(int id);

	Optional<User> findUserByUsername(String storeName);

	Page<User> findAllUsers(Pageable pageable);

	User update(User user);

	void deleteById(int id);
}
