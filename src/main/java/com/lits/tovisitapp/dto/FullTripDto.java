package com.lits.tovisitapp.dto;

import com.lits.tovisitapp.model.Place;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FullTripDto extends TripDto {
    private List<Place> places = new ArrayList<>();
}