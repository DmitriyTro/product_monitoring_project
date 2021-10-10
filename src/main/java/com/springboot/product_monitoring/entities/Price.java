package com.springboot.product_monitoring.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "price")
public class Price {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "unit_price")
	private int unitPrice;

	@Column(name = "date")
	private Timestamp date;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@ToString.Exclude
	private Product product = new Product();

	@ManyToOne
	@JoinColumn(name = "store_id")
	@ToString.Exclude
	private Store store = new Store();
}