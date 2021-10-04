package com.springboot.product_monitoring.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "category", schema = "public")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "category_name")
	private String categoryName;

	@Column(name = "description")
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "category_product",
			joinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
	@ToString.Exclude
	private List<Product> products = new ArrayList<>();
}