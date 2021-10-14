package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.entities.Role;
import com.springboot.product_monitoring.entities.User;
import com.springboot.product_monitoring.exceptions.errors.RoleErrorType;
import com.springboot.product_monitoring.exceptions.errors.UserErrorType;
import com.springboot.product_monitoring.exceptions.role.RoleException;
import com.springboot.product_monitoring.exceptions.user.UserException;
import com.springboot.product_monitoring.mappers.UserMapper;
import com.springboot.product_monitoring.repositories.RoleRepository;
import com.springboot.product_monitoring.repositories.UserRepository;
import com.springboot.product_monitoring.services.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder encoder;
	private final RoleRepository roleRepository;

	@Autowired
	public AdminServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder encoder, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.encoder = encoder;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDTO findUserById(int id) {
		User result = userRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method findUserById - no user found by id: {}", id);
			throw new UserException(String.format(UserErrorType.USER_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method findUserById - user found by id: {}", id);
		return userMapper.toUserDTO(result);
	}

	@Override
	public UserDTO findUserByUsername(String username) {
		User result = userRepository.findByUsername(username).orElse(null);

		if (result == null) {
			log.warn("IN method findUserByUsername - no user found by user name: {}", username);
			throw new UserException(String.format(UserErrorType.USER_BY_USERNAME_NOT_FOUND
					.getDescription(), username));
		}

		log.info("IN method findByStoreName - user: {} found by user name: {}", result, username);
		return userMapper.toUserDTO(result);
	}

	@Override
	public Page<UserDTO> findAllUsers(Pageable pageable) {
		Page<User> userPage = userRepository.findAll(pageable);
		List<UserDTO> usersDTOs = userMapper.toUsersDTOs(userPage.getContent());

		if (userPage.isEmpty()) {
			log.warn("IN method findAllUsers - no users found");
			throw new UserException(UserErrorType.USERS_NOT_FOUND.getDescription());
		}

		log.info("IN method findAllUsers - {} users found", userPage.getTotalElements());
		return new PageImpl<>(usersDTOs, pageable, userPage.getTotalElements());
	}

	@Override
	public UserDTO update(User user) {
		User userInDB = userRepository.findUserById(user.getId()).orElse(null);

		if (userInDB == null) {
			log.warn("IN method update - no user found by user name: {}", user.getUsername());
			throw new UserException(String.format(UserErrorType.USER_BY_USERNAME_NOT_FOUND
					.getDescription(), user.getUsername()));
		}

		userInDB.setUsername(user.getUsername());
		userInDB.setFirstName(user.getFirstName());
		userInDB.setLastName(user.getLastName());
		userInDB.setPassword(encoder.encode(user.getPassword()));
		userInDB.setEmail(user.getEmail());
		userInDB.getRoles()
				.addAll((user.getRoles()
						.stream()
						.map(role -> {
							Role userRole = roleRepository.findById(role.getId()).orElse(null);
							if (userRole == null) {
								log.warn("IN method update - no role found by id: {}",
										role.getId());
								throw new RoleException(String.format(RoleErrorType.ROLE_BY_ID_NOT_FOUND
												.getDescription(), role.getId()));
							}
							userRole.getUsers().add(userInDB);
							return userRole;
						})).collect(Collectors.toSet()));

		log.info("IN method update - user with id: {} updated successfully", userInDB.getId());
		return userMapper.toUserDTO(userRepository.save(userInDB));
	}

	@Override
	public void deleteById(int id) {
		User result = userRepository.findById(id).orElse(null);

		if (result == null) {
			log.warn("IN method deleteById - no user found by id: {}", id);
			throw new UserException(String.format(UserErrorType.USER_BY_ID_NOT_FOUND.getDescription(), id));
		}

		log.info("IN method deleteById user with id: {} successfully deleted", id);
		userRepository.deleteById(id);
	}
}