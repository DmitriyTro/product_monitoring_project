package com.springboot.product_monitoring.repository;

import com.springboot.product_monitoring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
}