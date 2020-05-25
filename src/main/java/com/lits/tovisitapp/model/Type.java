package com.lits.tovisitapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Type {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@ManyToMany(mappedBy = "types", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Builder.Default
	@ToString.Exclude
	private Set<Place> places = new HashSet<>();

	@Override
	// Implementing custom .equals with name only, because it's the only unique and not-null property
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Type type = (Type) o;
		return name.equals(type.name);
	}

	@Override
	// Implementing custom .hashCode with name only because it's the only unique and not-null property
	public int hashCode() {
		return Objects.hash(name);
	}
}
