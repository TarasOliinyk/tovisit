package com.lits.tovisitapp.dto;

import com.lits.tovisitapp.model.Place;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class TripPlaceDTO extends TripDTO {
    private List<Place> places = new ArrayList<>();
}