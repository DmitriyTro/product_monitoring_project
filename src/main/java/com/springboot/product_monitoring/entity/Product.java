package com.springboot.product_monitoring.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "product", schema = "public")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "product_name")
	private String productName;

	@ManyToMany(mappedBy = "products", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@ToString.Exclude
	private List<Category> categories;
}