package com.springboot.product_monitoring.service.impl;

import com.springboot.product_monitoring.entity.User;
import com.springboot.product_monitoring.exception.errors.UsersErrorType;
import com.springboot.product_monitoring.exception.users.UsersException;
import com.springboot.product_monitoring.repository.UserRepository;
import com.springboot.product_monitoring.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;

	@Autowired
	public AdminServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<User> findUserById(int id) {
		User result = userRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method findUserById - no user found by id: {}", id);
			throw new UsersException(String.format(UsersErrorType.USER_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method findUserById - user found by id: {}", id);
		return Optional.of(result);
	}

	@Override
	public Optional<User> findUserByUsername(String username) {
		User result = userRepository.findByUsername(username).orElse(null);

		if (result == null) {
			log.warn("IN method findUserByUsername - no user found by user name: {}", username);
			throw new UsersException(String.format(UsersErrorType.USER_BY_USERNAME_NOT_FOUND
					.getDescription(), username));
		}

		log.info("IN method findByStoreName - user: {} found by user name: {}", result, username);
		return Optional.of(result);
	}

	@Override
	public List<User> findAllUsers() {
		List<User> result = userRepository.findAll();

		if (result.isEmpty()) {
			log.warn("IN method findAllUsers - no users found");
			throw new UsersException(UsersErrorType.USERS_NOT_FOUND.getDescription());

		}

		log.info("IN method findAllUsers - {} users found", result.size());
		return result;
	}

	@Override
	public User update(User user) {
		return null;
	}

	@Override
	public void deleteById(int id) {
		User result = userRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method deleteById - no user found by id: {}", id);
			throw new UsersException(String.format(UsersErrorType.USER_BY_ID_NOT_FOUND.getDescription(), id));

		}

		log.info("IN method deleteById user with id: {} successfully deleted", id);
		userRepository.deleteById(id);
	}
}