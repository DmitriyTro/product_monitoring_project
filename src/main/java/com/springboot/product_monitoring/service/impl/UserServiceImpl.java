package com.springboot.product_monitoring.service.impl;

import com.springboot.product_monitoring.entity.User;
import com.springboot.product_monitoring.repository.UserRepository;
import com.springboot.product_monitoring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> findAllUsers() {
		List<User> result = userRepository.findAll();

		if (result.size() == 0) {
			log.warn("IN method findAllUsers - no user found");
			return null;
		}

		log.info("IN method getAllUser - {} users found", result.size());

		return result;
	}

	@Override
	public User findByUsername(String username) {
		User result = userRepository.findByUsername(username);

		if (result == null) {
			log.warn("IN method findByUsername - no user found by username: {}", username);
			return null;
		}

		log.info("IN method findByUsername - user: {} found by username: {}", result, username);

		return result;
	}

	@Override
	public User findById(int id) {
		User result = userRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method findById - no user found by id: {}", id);
			return null;
		}

		log.info("IN method findById - user found by id: {}", result);
		return result;
	}

	@Override
	public void deleteById(int id) {
		userRepository.deleteById(id);

		log.info("IN method delete - user with id: {} successfully deleted", id);
	}
}