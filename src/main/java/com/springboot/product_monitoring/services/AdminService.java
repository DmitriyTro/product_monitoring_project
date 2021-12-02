package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

	UserDTO findUserById(int id);

	UserDTO findUserByUsername(String username);

	Page<UserDTO> findAllUsers(Pageable pageable);

	UserDTO update(User user);

	MessageResponse deleteById(int id);
}
