package com.springboot.product_monitoring.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "price")
public class Price {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotBlank
	@Column(name = "unit_price")
	private int unitPrice;

	@NotBlank
	@Column(name = "date")
	private LocalDateTime date;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@ToString.Exclude
	private Product product;

	@ManyToOne
	@JoinColumn(name = "store_id")
	@ToString.Exclude
	private Store store;
}