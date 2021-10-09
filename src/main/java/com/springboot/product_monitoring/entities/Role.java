package com.springboot.product_monitoring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_type")
	private ERole roleType;

	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	@ToString.Exclude
	private Set<User> users = new HashSet<>();
}