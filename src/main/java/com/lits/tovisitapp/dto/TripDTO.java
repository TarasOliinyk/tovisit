package com.lits.tovisitapp.dto;

import com.lits.tovisitapp.model.Place;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class TripDTO {
    private Long id;
    private Long accountId;
    private String name;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private List<Place> places = new ArrayList<>();
}
