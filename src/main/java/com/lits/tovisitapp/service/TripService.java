package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.FullTripDTO;
import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.dto.ShortTripDTO;
import com.lits.tovisitapp.dto.TripDTO;

import java.util.List;

public interface TripService {

    TripDTO create(TripDTO tripDto);

    TripDTO update(TripDTO newTripDto);

    void delete(Long id);

    FullTripDTO getOne(Long id);

    List<ShortTripDTO> getAll();

    List<ShortTripDTO> getAllByUserId(Long userId);

    FullTripDTO addPlacesToTrip(Long tripId, List<PlaceDTO> placeDTOs);

    FullTripDTO deletePlaceFromTrip(Long tripId, Long placeId);

    FullTripDTO addPlaceToTrip(Long tripId, PlaceDTO placeDTO);

}
