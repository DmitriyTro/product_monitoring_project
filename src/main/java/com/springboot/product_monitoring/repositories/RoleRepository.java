package com.springboot.product_monitoring.repositories;

import com.springboot.product_monitoring.entities.ERole;
import com.springboot.product_monitoring.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByRoleType(ERole roleType);
}
