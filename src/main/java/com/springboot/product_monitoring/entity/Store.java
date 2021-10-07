package com.springboot.product_monitoring.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "store")
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "store_name")
	private String storeName;
}