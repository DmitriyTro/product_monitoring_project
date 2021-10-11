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
@Table(name = "store")
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotBlank
	@Size(max = 100)
	@Column(name = "store_name")
	private String storeName;

	@JsonIgnore
	@OneToMany(mappedBy = "store")
	@ToString.Exclude
	private List<Price> prices = new ArrayList<>();
}