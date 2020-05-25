package com.lits.tovisitapp.unit.impl;

import com.lits.tovisitapp.dto.FullTripDto;
import com.lits.tovisitapp.dto.PlaceForTripDto;
import com.lits.tovisitapp.dto.ShortTripDto;
import com.lits.tovisitapp.dto.TripDto;
import com.lits.tovisitapp.exceptions.TripNotFoundException;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.repository.TripRepository;
import com.lits.tovisitapp.unit.TripService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class TripServiceImpl implements TripService {

    private final ModelMapper modelMapper;
    private final TripRepository tripRepository;

    public TripServiceImpl(ModelMapper modelMapper, TripRepository tripRepository) {
        this.modelMapper = modelMapper;
        this.tripRepository = tripRepository;
    }

    @Override
    public TripDto create(TripDto tripDto) {
        Trip trip = modelMapper.map(tripDto, Trip.class);
        return modelMapper.map(tripRepository.save(trip), TripDto.class);
    }

    @Override
    public TripDto update(TripDto newTripDto) {
        Trip newTrip = modelMapper.map(newTripDto, Trip.class);
        return modelMapper.map(tripRepository.save(newTrip), TripDto.class);
    }

    @Override
    public void delete(Long id) {
        tripRepository.deleteById(id);
    }

    @Override
    public FullTripDto getOne(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", id)));
        return modelMapper.map(trip, FullTripDto.class);
    }

    @Override
    public List<ShortTripDto> getAll() {
        List<Trip> trips = tripRepository.findAll();

        List<ShortTripDto> shortTripDtos = trips.stream().map(trip -> {
            List<Long> placeIds = trip.getPlaces().stream().map(Place::getId).collect(Collectors.toList());
            ShortTripDto shortTripDto = modelMapper.map(trip, ShortTripDto.class);
            shortTripDto.setPlaceIds(placeIds);
            return shortTripDto;
        }).collect(Collectors.toList());

        return shortTripDtos;
    }

    @Override
    public List<ShortTripDto> getAllByAccountId(Long accountId) {
        List<Trip> trips = tripRepository.findAllByAccountId(accountId);

        List<ShortTripDto> shortTripDtos = trips.stream().map(trip -> {
            List<Long> placeIds = trip.getPlaces().stream().map(Place::getId).collect(Collectors.toList());
            ShortTripDto shortTripDto = modelMapper.map(trip, ShortTripDto.class);
            shortTripDto.setPlaceIds(placeIds);
            return shortTripDto;
        }).collect(Collectors.toList());

        return shortTripDtos;
    }

    @Override
    @Transactional
    public FullTripDto addPlacesToTrip(Long tripId, List<PlaceForTripDto> placeForTripDtos) {

        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));

        List<Place> newPlaces = placeForTripDtos.stream().map(placeForTripDto -> {
            placeForTripDto.setTrip(trip);
            return modelMapper.map(placeForTripDto, Place.class);
        }).collect(Collectors.toList());

        List<Place> places = trip.getPlaces();
        for (Place place : newPlaces) {
            places.add(place);
        }

        tripRepository.save(trip);

        return modelMapper.map(trip, FullTripDto.class);
    }

    @Override
    public FullTripDto addPlaceToTrip(Long tripId, PlaceForTripDto placeForTripDto) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
        Place newPlace = modelMapper.map(placeForTripDto, Place.class);
        newPlace.setTrip(trip);
        trip.getPlaces().add(newPlace);
        tripRepository.save(trip);

        return modelMapper.map(trip, FullTripDto.class);
    }

    @Override
    public FullTripDto deletePlaceFromTrip(Long tripId, Long placeId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
        List<Place> places = trip.getPlaces().stream().filter(place -> place.getId() != placeId).collect(Collectors.toList());
        trip.setPlaces(places);
        tripRepository.save(trip);
        return modelMapper.map(trip, FullTripDto.class);
    }


}
