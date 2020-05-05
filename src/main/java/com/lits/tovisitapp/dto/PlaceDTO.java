package com.lits.tovisitapp.dto;

import com.lits.tovisitapp.model.Type;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlaceDTO {
    private Long id;
    private Long tripId;
    private String googlePlaceId;
    private String parentLocation;
    private String name;
    private String formattedAddress;
    private BigDecimal locationLat;
    private BigDecimal locationLng;
    private BigDecimal priceLevel;
    private BigDecimal rating;
    private List<Type> types = new ArrayList<>();
}
