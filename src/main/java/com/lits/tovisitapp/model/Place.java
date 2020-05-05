package com.lits.tovisitapp.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "tripId")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tripId;

    @Column(nullable = false)
    private String googlePlaceId;

    @Column
    private String parentLocation;

    @Column(nullable = false)
    @NotNull(message = "Place name cannot be null")
    @NotEmpty(message = "Place name cannot be empty")
    private String name;

    @Column(nullable = false)
    private String formattedAddress;

    @Column(nullable = false)
    private BigDecimal locationLat;

    @Column(nullable = false)
    private BigDecimal locationLng;

    @Column(nullable = false)
    private BigDecimal priceLevel;

    @Column(nullable = false)
    private BigDecimal rating;

    @OneToMany
    private List<Type> types = new ArrayList<>();
}
