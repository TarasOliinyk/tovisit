package com.lits.tovisitapp.unit;

import com.lits.tovisitapp.dto.FullTripDto;
import com.lits.tovisitapp.dto.PlaceForTripDto;
import com.lits.tovisitapp.dto.ShortTripDto;
import com.lits.tovisitapp.dto.TripDto;

import java.util.List;

public interface TripService {

    TripDto create(TripDto tripDto);

    TripDto update(TripDto newTripDto);

    void delete(Long id);

    FullTripDto getOne(Long id);

    List<ShortTripDto> getAll();

    List<ShortTripDto> getAllByAccountId(Long accountId);

    FullTripDto addPlacesToTrip(Long tripId, List<PlaceForTripDto> placeForTripDtos);

    FullTripDto deletePlaceFromTrip(Long tripId, Long placeId);

    FullTripDto addPlaceToTrip(Long tripId, PlaceForTripDto placeForTripDto);

}
