package com.lits.tovisitapp.dto;

import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.model.Type;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlaceForTripDTO {

    private Long id;
    private String googlePlaceId;
    private String parentLocation;
    private String name;
    private String formattedAddress;
    private BigDecimal locationLat;
    private BigDecimal locationLng;
    private Integer priceLevel;
    private BigDecimal rating;
    private Trip trip;
    private List<Type> types = new ArrayList<>();
}
