package com.lits.tovisitapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false, precision = 15, scale = 15)
    private BigDecimal locationLat;

    @Column(nullable = false, precision = 15, scale = 15)
    private BigDecimal locationLng;

    @Column(nullable = false)
    private Integer priceLevel;

    @Column(nullable = false)
    private BigDecimal rating;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @ManyToMany(mappedBy = "places", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Type> types = new ArrayList<>();
}
