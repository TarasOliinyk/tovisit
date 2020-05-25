package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.FullTripDto;
import com.lits.tovisitapp.dto.PlaceForTripDto;
import com.lits.tovisitapp.dto.ShortTripDto;
import com.lits.tovisitapp.dto.TripDto;
import com.lits.tovisitapp.unit.TripService;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequestMapping("/trips")
@Log
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/trip")
    public TripDto create(@Valid @RequestBody TripDto tripDto) {
        log.info("Create new trip");
        return tripService.create(tripDto);
    }

    @GetMapping("/trip/{tripId}")
    public FullTripDto getOne(@PathVariable(name = "tripId") Long tripId) {
        log.info(format("Get trip by tripId = %d", tripId));
        return tripService.getOne(tripId);
    }

    @GetMapping
    public List<ShortTripDto> getAll() {
        log.info("Get all trips");
        return tripService.getAll();
    }

    @GetMapping("/account/{accountId}")
    public List<ShortTripDto> getAllByAccountId(@PathVariable(name = "accountId") Long accountId) {
        log.info(format("Get all trips by accountId = %d", accountId));
        return tripService.getAllByAccountId(accountId);
    }

    @PutMapping("/trip/{tripId}")
    public TripDto update(@PathVariable(name = "tripId") Long tripId, @Valid @RequestBody TripDto newTripDto) {
        newTripDto.setId(tripId);
        log.info(format("Update trip by tripId = %d", tripId));
        return tripService.update(newTripDto);
    }

    @PutMapping("/trip/{tripId}/places")
    public FullTripDto addPlacesToTrip(@PathVariable(name = "tripId") Long tripId, @RequestBody List<PlaceForTripDto> placeForTripDtos) {
        log.info(format("Add places to trip by tripId = %d", tripId));
        return tripService.addPlacesToTrip(tripId, placeForTripDtos);
    }

    @PutMapping("/trip/{tripId}/place")
    public FullTripDto addPlaceToTrip(@PathVariable(name = "tripId") Long tripId, @RequestBody PlaceForTripDto placeForTripDto) {
        log.info(format("Add place to trip by tripId = %d", tripId));
        return tripService.addPlaceToTrip(tripId, placeForTripDto);
    }

    @DeleteMapping("/trip/{tripId}")
    public void delete(@PathVariable(name = "tripId") Long tripId) {
        log.info(format("Delete trip by tripId = %d", tripId));
        tripService.delete(tripId);
    }

    @DeleteMapping("/trip/{tripId}/places/{placeId}")
    public FullTripDto deletePlacesFromTrip(@PathVariable(name = "tripId") Long tripId, @PathVariable(name = "placeId") Long placeId) {
        log.info(format("Delete place from trip by tripId = %d", tripId));
        return tripService.deletePlaceFromTrip(tripId, placeId);
    }

}
