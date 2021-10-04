package com.springboot.product_monitoring.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role", schema = "public")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "role_name")
	private String roleName;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@ToString.Exclude
	private Set<User> users = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Role role = (Role) o;
		return Objects.equals(id, role.id);
	}

	@Override
	public int hashCode() {
		return id;
	}
}