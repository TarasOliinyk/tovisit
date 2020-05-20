package com.lits.tovisitapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Type {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@Column(nullable = false, unique = true)
	private String name;

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "place_type",
			joinColumns = @JoinColumn(name = "type_id"),
			inverseJoinColumns = @JoinColumn(name = "place_id"))
	@Builder.Default
	private List<Place> places = new ArrayList<>();
}
