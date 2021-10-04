package com.springboot.product_monitoring.controller;

import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.entity.User;
import com.springboot.product_monitoring.mappers.UserMapper;
import com.springboot.product_monitoring.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final UserMapper userMapper;

	public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@GetMapping(value = "/list")
	public ResponseEntity<List<UserDTO>> findAllUsers() {
		List<User> allUsers = userService.findAllUsers();

		if(allUsers == null){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(userMapper.toDTOList(allUsers), HttpStatus.OK);
	}

	@GetMapping(value = "/name/{username}")
	public ResponseEntity<UserDTO> findByUsername(@PathVariable(name = "username")String username) {
		User user = userService.findByUsername(username);

		if(user == null){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable(name = "id") int id){
		User user = userService.findById(id);

		if(user == null){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
	}

	@DeleteMapping(value = "{id}")
	public String deleteById(@PathVariable(name = "id") int id) {
		userService.deleteById(id);
		return "User with id: " + id + " was deleted.";
	}
}
