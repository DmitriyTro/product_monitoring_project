package com.springboot.product_monitoring.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "price", schema = "public")
public class Price {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "unit_price")
	private String unitPrice;

	@Column(name = "date")
	private LocalDateTime date;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@ToString.Exclude
	private Product product;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "store_id")
	@ToString.Exclude
	private Store store;
}