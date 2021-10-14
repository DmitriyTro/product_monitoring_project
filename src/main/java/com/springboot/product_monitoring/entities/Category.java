package com.springboot.product_monitoring.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotBlank
	@Size(max = 30)
	@Column(name = "category_name")
	private String categoryName;

	@ManyToMany
	@JoinTable(name = "category_product",
			joinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
	@ToString.Exclude
	private List<Product> products = new ArrayList<>();
}