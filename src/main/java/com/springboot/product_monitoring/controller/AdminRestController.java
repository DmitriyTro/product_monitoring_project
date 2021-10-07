package com.springboot.product_monitoring.controller;

import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.entity.User;
import com.springboot.product_monitoring.exception.users.UsersCustomExceptionHandler;
import com.springboot.product_monitoring.mappers.UserMapper;
import com.springboot.product_monitoring.payload.response.MessageResponse;
import com.springboot.product_monitoring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@UsersCustomExceptionHandler
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

	private final AdminService adminService;
	private final UserMapper userMapper;

	@Autowired
	public AdminRestController(AdminService adminService, UserMapper userMapper) {
		this.adminService = adminService;
		this.userMapper = userMapper;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable(name = "id") int id) {
		User result = adminService.findUserById(id).get();
		return new ResponseEntity<>(userMapper.toDTO(result), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/users/name/{username}")
	public ResponseEntity<UserDTO> findByUsername(@PathVariable(name = "username") String username) {
		User result = adminService.findUserByUsername(username).get();
		return new ResponseEntity<>(userMapper.toDTO(result), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/users/list")
	public ResponseEntity<List<UserDTO>> findAllUsers() {
		List<User> result = adminService.findAllUsers();
		return new ResponseEntity<>(userMapper.toDTOList(result), HttpStatus.OK);
	}


	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/users/update")
	public ResponseEntity<UserDTO> update(@RequestBody User user) {
		return null;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/users/delete/{id}")
	public ResponseEntity deleteById(@PathVariable(name = "id") int id) {
		adminService.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
	}
}