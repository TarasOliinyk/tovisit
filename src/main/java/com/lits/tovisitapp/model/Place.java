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
@AllArgsConstructor
@Builder
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

	@Column(nullable = false, precision=15, scale=15)
	private BigDecimal locationLat;

	@Column(nullable = false, precision=15, scale=15)
	private BigDecimal locationLng;

	@Column(nullable = false)
	private Integer priceLevel;

	@Column(nullable = false)
	private BigDecimal rating;

	@Column(name = "trip_id", insertable = false, updatable = false/*, nullable = false*/)
	private Long tripId;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Trip trip;

	public void setTrip(Trip trip) {
		this.trip = trip;
		this.tripId = (trip != null && trip.getId() != null) ? trip.getId() : null;
	}

	@ManyToMany(mappedBy = "places", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Builder.Default
	private List<Type> types = new ArrayList<>();
}
