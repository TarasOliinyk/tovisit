package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.CreatePlaceDto;
import com.lits.tovisitapp.dto.TripDto;
import com.lits.tovisitapp.dto.TripPlaceDto;
import com.lits.tovisitapp.service.TripService;
import lombok.extern.java.Log;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequestMapping("/trips")
@Log
@Validated
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
    public TripPlaceDto getOne(@PathVariable(name = "tripId") @Positive Long tripId) {
        log.info(format("Get trip by tripId = %d", tripId));
        return tripService.getOne(tripId);
    }

    @GetMapping
    public List<TripDto> getAll() {
        log.info("Get all trips");
        return tripService.getAll();
    }

    @GetMapping("/account/{accountId}")
    public List<TripDto> getAllByAccountId(@PathVariable(name = "accountId") @Positive Long accountId) {
        log.info(format("Get all trips by accountId = %d", accountId));
        return tripService.getAllByAccountId(accountId);
    }

    @PutMapping("/trip/{tripId}")
    public TripDto update(@PathVariable(name = "tripId") @Positive Long tripId, @Valid @RequestBody TripDto newTripDto) {
        newTripDto.setId(tripId);
        log.info(format("Update trip by tripId = %d", tripId));
        return tripService.update(newTripDto);
    }

    @PutMapping("/trip/{tripId}/places")
    public TripPlaceDto addPlacesToTrip(@PathVariable(name = "tripId") @Positive Long tripId, @RequestBody List<CreatePlaceDto> createPlaceDtos) {
        log.info(format("Add places to trip by tripId = %d", tripId));
        return tripService.addPlacesToTrip(tripId, createPlaceDtos);
    }

    @DeleteMapping("/trip/{tripId}")
    public void delete(@PathVariable(name = "tripId") @Positive Long tripId) {
        log.info(format("Delete trip by tripId = %d", tripId));
        tripService.delete(tripId);
    }

    @DeleteMapping("/trip/{tripId}/places/{placeId}")
    public TripPlaceDto deletePlacesFromTrip(@PathVariable(name = "tripId") @Positive Long tripId, @PathVariable(name = "placeId") @Positive Long placeId) {
        log.info(format("Delete place by placeId = %d from trip by tripId = %d", placeId, tripId));
        return tripService.deletePlaceFromTrip(tripId, placeId);
    }

}
