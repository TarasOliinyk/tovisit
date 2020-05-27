package com.lits.tovisitapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

//	@Column(name = "trip_id", insertable = false, updatable = false, nullable = false)
//	private Long tripId;

//	@JsonManagedReference
	@JsonBackReference
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Trip trip;

//	public void setTrip(Trip trip) {
//		this.trip = trip;
//		this.tripId = (trip != null && trip.getId() != null) ? trip.getId() : null;
//	}

	@ManyToMany(mappedBy = "places", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Builder.Default
	private List<Type> types = new ArrayList<>();
}
