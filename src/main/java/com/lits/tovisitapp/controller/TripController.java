package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.FullTripDTO;
import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.dto.ShortTripDTO;
import com.lits.tovisitapp.dto.TripDTO;
import com.lits.tovisitapp.service.TripService;
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
    public TripDTO create(@Valid @RequestBody TripDTO tripDto) {
        log.info("Create new trip");
        return tripService.create(tripDto);
    }

    @GetMapping("/trip/{tripId}")
    public FullTripDTO getOne(@PathVariable(name = "tripId") Long tripId) {
        log.info(format("Get trip by tripId = %d", tripId));
        return tripService.getOne(tripId);
    }

    @GetMapping
    public List<ShortTripDTO> getAll() {
        log.info("Get all trips");
        return tripService.getAll();
    }

    @GetMapping("/account/{userId}")
    public List<ShortTripDTO> getAllByAccountId(@PathVariable(name = "userId") Long userId) {
        log.info(format("Get all trips by accountId = %d", userId));
        return tripService.getAllByUserId(userId);
    }

    @PutMapping("/trip/{tripId}")
    public TripDTO update(@PathVariable(name = "tripId") Long tripId, @Valid @RequestBody TripDTO newTripDto) {
        newTripDto.setId(tripId);
        log.info(format("Update trip by tripId = %d", tripId));
        return tripService.update(newTripDto);
    }

    @PutMapping("/trip/{tripId}/places")
    public FullTripDTO addPlacesToTrip(@PathVariable(name = "tripId") Long tripId, @Valid @RequestBody List<PlaceDTO> placeDTOs) {
        log.info(format("Add places to trip by tripId = %d", tripId));
        return tripService.addPlacesToTrip(tripId, placeDTOs);
    }

    @PutMapping("/trip/{tripId}/place")
    public FullTripDTO addPlaceToTrip(@PathVariable(name = "tripId") Long tripId, @Valid @RequestBody PlaceDTO placeDTO) {
        log.info(format("Add place to trip by tripId = %d", tripId));
        return tripService.addPlaceToTrip(tripId, placeDTO);
    }

    @DeleteMapping("/trip/{tripId}")
    public void delete(@PathVariable(name = "tripId") Long tripId) {
        log.info(format("Delete trip by tripId = %d", tripId));
        tripService.delete(tripId);
    }

    @DeleteMapping("/trip/{tripId}/places/{placeId}")
    public FullTripDTO deletePlacesFromTrip(@PathVariable(name = "tripId") Long tripId, @PathVariable(name = "placeId") Long placeId) {
        log.info(format("Delete place from trip by tripId = %d", tripId));
        return tripService.deletePlaceFromTrip(tripId, placeId);
    }

}
