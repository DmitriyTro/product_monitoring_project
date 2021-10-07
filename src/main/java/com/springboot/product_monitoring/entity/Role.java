package com.springboot.product_monitoring.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Enumerated(EnumType.STRING)
	@Column
	private ERole name;
}