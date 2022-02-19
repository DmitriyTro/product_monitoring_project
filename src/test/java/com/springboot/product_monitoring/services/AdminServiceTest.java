package com.springboot.product_monitoring.services;

import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.User;
import com.springboot.product_monitoring.mappers.UserMapper;
import com.springboot.product_monitoring.repositories.RoleRepository;
import com.springboot.product_monitoring.repositories.UserRepository;
import com.springboot.product_monitoring.services.impl.AdminServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AdminServiceTest {

	@MockBean
	UserRepository userRepository;

	@MockBean
	RoleRepository roleRepository;

	@MockBean
	PasswordEncoder passwordEncoder;

	@MockBean
	UserMapper userMapper;

	@Captor
	ArgumentCaptor<User> argumentCaptor;

	@MockBean
	AdminServiceImpl adminService;

	User user;

	UserDTO userDTO;

	List<User> users;

	@BeforeEach
	void setUp() {
		adminService = new AdminServiceImpl(userRepository, userMapper, passwordEncoder, roleRepository);

		user = new User();
		user.setId(1);
		user.setUsername("testuser");
		user.setEmail("user@gmail.com");

		userDTO = new UserDTO();
		userDTO.setId(1);
		userDTO.setUsername("testuser");
		userDTO.setEmail("user@gmail.com");
	}

	@AfterEach
	void tearDown() {

		user = null;
		userDTO = null;
	}

	@Test
	@DisplayName("Should return user by id")
	void findUserById() {

		when(userRepository.findUserById(1)).thenReturn(user);
		when(userMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

		UserDTO actualUserDTO = adminService.findUserById(1);

		assertThat(actualUserDTO.getId()).isEqualTo(1);
		assertThat(actualUserDTO.getEmail()).isEqualTo("user@gmail.com");
		assertThat(actualUserDTO.getUsername()).isEqualTo("testuser");
	}

	@Test
	@DisplayName("Should return user by user name")
	void findUserByUsername() {

		when(userRepository.findByUsername("testuser")).thenReturn(Optional.ofNullable(user));
		when(userMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

		UserDTO actualUserDTO = adminService.findUserByUsername("testuser");

		assertThat(actualUserDTO.getId()).isEqualTo(1);
		assertThat(actualUserDTO.getEmail()).isEqualTo("user@gmail.com");
		assertThat(actualUserDTO.getUsername()).isEqualTo("testuser");
	}

	@Test
	@DisplayName("Should return list of users")
	void findAllUsers() {

		PageRequest pageRequest =  PageRequest.of(0, 10);
		users = new ArrayList<>(List.of(user, user));
		Page<User> userPage = new PageImpl<>(users, pageRequest, users.size());

		when(userRepository.findAll(pageRequest)).thenReturn(userPage);
		Page<UserDTO> actualUserPage = adminService.findAllUsers(pageRequest);

		assertThat(userPage.getContent().get(0).getId()).isEqualTo(1);
		assertThat(userPage.getContent().size()).isEqualTo(2);
		assertThat(actualUserPage.getPageable().getPageSize()).isEqualTo(10);
		assertThat(actualUserPage.getTotalElements()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should update user")
	void update() {

		when(userRepository.findUserById(1)).thenReturn(user);

		user.setUsername("changedusername");
		adminService.update(user);
		verify(userRepository, times(1)).save(argumentCaptor.capture());

		assertThat(argumentCaptor.getValue().getId()).isEqualTo(1);
		assertThat(argumentCaptor.getValue().getEmail()).isEqualTo("user@gmail.com");
		assertThat(argumentCaptor.getValue().getUsername()).isEqualTo("changedusername");
	}

	@Test
	@DisplayName("Should delete user")
	void deleteById() {

		when(userRepository.findById(1)).thenReturn(Optional.of(user));

		MessageResponse actualStore = adminService.deleteById(user.getId());

		assertEquals("User deleted successfully!", actualStore.getMessage());
	}
}