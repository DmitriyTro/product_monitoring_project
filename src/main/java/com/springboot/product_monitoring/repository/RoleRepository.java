package com.springboot.product_monitoring.repository;

import com.springboot.product_monitoring.entity.ERole;
import com.springboot.product_monitoring.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(ERole name);
}
