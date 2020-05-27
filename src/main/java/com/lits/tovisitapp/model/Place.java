package com.lits.tovisitapp.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Place {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String googlePlaceId;

	@Column
	private String parentLocation;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String formattedAddress;

	@Column(nullable = false)
	private Double locationLat;

	@Column(nullable = false)
	private Double locationLng;

	@Column
	private Integer priceLevel;

	@Column
	private Double rating;

	@Column(name = "trip_id", insertable = false, updatable = false, nullable = false)
	private Long tripId;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Trip trip;

	public void setTrip(Trip trip) {
		this.trip = trip;
		this.tripId = (trip != null && trip.getId() != null) ? trip.getId() : null;
	}

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "place_type",
			joinColumns = @JoinColumn(name = "type_id"),
			inverseJoinColumns = @JoinColumn(name = "place_id"))
	private Set<Type> types = new HashSet<>();
}
