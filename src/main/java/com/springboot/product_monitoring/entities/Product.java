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
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotBlank
	@Size(max = 30)
	@Column(name = "product_name")
	private String productName;

	@ManyToMany(mappedBy = "products")
	@ToString.Exclude
	private List<Category> categories = new ArrayList<>();
}