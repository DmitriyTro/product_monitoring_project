package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.dto.payload.request.LoginRequest;
import com.springboot.product_monitoring.dto.payload.request.SignupRequest;
import com.springboot.product_monitoring.dto.payload.response.JwtResponse;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.ERole;
import com.springboot.product_monitoring.entities.Role;
import com.springboot.product_monitoring.entities.User;
import com.springboot.product_monitoring.exceptions.errors.RoleErrorType;
import com.springboot.product_monitoring.exceptions.role.RoleCustomExceptionHandler;
import com.springboot.product_monitoring.exceptions.role.RoleException;
import com.springboot.product_monitoring.repositories.RoleRepository;
import com.springboot.product_monitoring.repositories.UserRepository;
import com.springboot.product_monitoring.security.jwt.JwtUtils;
import com.springboot.product_monitoring.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RoleCustomExceptionHandler
@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationRestController {

	private final AuthenticationManager authenticationManager;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder encoder;

	private final JwtUtils jwtUtils;

	@Autowired
	public AuthenticationRestController(
			AuthenticationManager authenticationManager,
			UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder encoder,
			JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getFirstName(),
				userDetails.getLastName(),
				userDetails.getEmail(),
				roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail());

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByRoleType(ERole.ROLE_USER)
					.orElseThrow(() -> new RoleException(RoleErrorType.ROLE_NOT_FOUND.getDescription()));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				if ("admin".equals(role)) {
					Role adminRole = roleRepository.findByRoleType(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RoleException(RoleErrorType.ROLE_NOT_FOUND.getDescription()));
					roles.add(adminRole);
				} else {
					Role userRole = roleRepository.findByRoleType(ERole.ROLE_USER)
							.orElseThrow(() -> new RoleException(RoleErrorType.ROLE_NOT_FOUND.getDescription()));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}