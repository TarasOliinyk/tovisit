package com.lits.tovisitapp.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShortTripDto extends TripDto {
    private List<Long> placeIds = new ArrayList<>();
}
