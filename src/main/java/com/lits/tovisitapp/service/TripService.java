package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.CreatePlaceDto;
import com.lits.tovisitapp.dto.TripDto;
import com.lits.tovisitapp.dto.TripPlaceDto;

import java.util.List;

public interface TripService {

    TripDto create(TripDto tripDto);

    TripDto update(TripDto newTripDto);

    void delete(Long id);

    TripPlaceDto getOne(Long id);

    List<TripDto> getAll();

    List<TripDto> getAllByAccountId(Long accountId);

    TripPlaceDto addPlacesToTrip(Long tripId, List<CreatePlaceDto> createPlaceDtos);

    TripPlaceDto deletePlaceFromTrip(Long tripId, Long placeId);

}
