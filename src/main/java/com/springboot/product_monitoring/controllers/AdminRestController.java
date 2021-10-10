package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.User;
import com.springboot.product_monitoring.exceptions.user.UserCustomExceptionHandler;
import com.springboot.product_monitoring.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@UserCustomExceptionHandler
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

	private final AdminService adminService;

	@Autowired
	public AdminRestController(AdminService adminService) {
		this.adminService = adminService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable(name = "id") int id) {
		return new ResponseEntity<>(adminService.findUserById(id), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/users/name/{username}")
	public ResponseEntity<UserDTO> findByUsername(@PathVariable(name = "username") String username) {
		return new ResponseEntity<>(adminService.findUserByUsername(username), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/users/list")
	public Page<UserDTO> findAllUsers(
			@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
		return adminService.findAllUsers(pageable);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/users/update")
	public ResponseEntity<UserDTO> updateUser(@Validated @RequestBody User user) {
		return new ResponseEntity<>(adminService.updateUser(user), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/users/delete/{id}")
	public ResponseEntity deleteById(@PathVariable(name = "id") int id) {
		adminService.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
	}
}