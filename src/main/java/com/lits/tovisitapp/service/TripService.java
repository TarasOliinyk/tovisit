package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.dto.TripDTO;
import com.lits.tovisitapp.dto.TripPlaceDTO;

import java.util.List;

public interface TripService {

    TripDTO create(TripDTO tripDto);

    TripDTO update(TripDTO newTripDto);

    void delete(Long id);

    TripPlaceDTO getOne(Long id);

    List<TripDTO> getAll();

    List<TripDTO> getAllByUserId(Long userId);

    TripPlaceDTO addPlacesToTrip(Long tripId, List<PlaceDTO> placeDTOs);

    TripPlaceDTO deletePlaceFromTrip(Long tripId, Long placeId);
}
