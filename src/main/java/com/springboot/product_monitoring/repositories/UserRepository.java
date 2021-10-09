package com.springboot.product_monitoring.repositories;

import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	Optional<UserDTO> updateUser(User user);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}