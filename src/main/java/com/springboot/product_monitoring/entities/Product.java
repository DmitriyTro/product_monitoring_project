package com.springboot.product_monitoring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@JsonIgnore
	@ManyToMany(mappedBy = "products", cascade = {CascadeType.PERSIST,
			CascadeType.MERGE})
	@ToString.Exclude
	public List<Category> categories = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	@ToString.Exclude
	public List<Price> prices = new ArrayList<>();
}