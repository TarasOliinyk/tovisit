package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.FullTripDTO;
import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.dto.ShortTripDTO;
import com.lits.tovisitapp.dto.TripDTO;
import com.lits.tovisitapp.exceptions.trip.TripNotFoundException;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.repository.TripRepository;
import com.lits.tovisitapp.service.PlaceService;
import com.lits.tovisitapp.service.TripService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class TripServiceImpl implements TripService {

    private final ModelMapper modelMapper;
    private final TripRepository tripRepository;

    private PlaceService placeService;

    public TripServiceImpl(ModelMapper modelMapper, TripRepository tripRepository, PlaceService placeService) {
        this.modelMapper = modelMapper;
        this.tripRepository = tripRepository;
        this.placeService = placeService;
    }

    @Override
    public TripDTO create(TripDTO tripDto) {
        Trip trip = modelMapper.map(tripDto, Trip.class);
        return modelMapper.map(tripRepository.save(trip), TripDTO.class);
    }

    @Override
    public TripDTO update(TripDTO newTripDto) {
        Trip newTrip = modelMapper.map(newTripDto, Trip.class);
        return modelMapper.map(tripRepository.save(newTrip), TripDTO.class);
    }

    @Override
    public void delete(Long id) {
        tripRepository.deleteById(id);
    }

    @Override
    public FullTripDTO getOne(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", id)));
        return modelMapper.map(trip, FullTripDTO.class);
    }

    @Override
    public List<ShortTripDTO> getAll() {
        List<Trip> trips = tripRepository.findAll();

        List<ShortTripDTO> shortTripDtos = trips.stream().map(trip -> {
            List<Long> placeIds = trip.getPlaces().stream().map(Place::getId).collect(Collectors.toList());
            ShortTripDTO shortTripDto = modelMapper.map(trip, ShortTripDTO.class);
            shortTripDto.setPlaceIds(placeIds);
            return shortTripDto;
        }).collect(Collectors.toList());

        return shortTripDtos;
    }

    @Override
    public List<ShortTripDTO> getAllByUserId(Long accountId) {
        List<Trip> trips = tripRepository.findAllByUserId(accountId);

        List<ShortTripDTO> shortTripDtos = trips.stream().map(trip -> {
            List<Long> placeIds = trip.getPlaces().stream().map(Place::getId).collect(Collectors.toList());
            ShortTripDTO shortTripDto = modelMapper.map(trip, ShortTripDTO.class);
            shortTripDto.setPlaceIds(placeIds);
            return shortTripDto;
        }).collect(Collectors.toList());

        return shortTripDtos;
    }

    @Override
    public FullTripDTO addPlacesToTrip(Long tripId, List<PlaceDTO> placeDTOs) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));

        placeDTOs.forEach(p -> {
            p.setTripId(tripId);
            placeService.savePlace(p);
        });

        return modelMapper.map(trip, FullTripDTO.class);
    }

//    @Override
//    @Transactional
//    public FullTripDTO addPlacesToTrip(Long tripId, List<PlaceForTripDTO> placeForTripDtos) {
//
//        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
//
//        List<Place> newPlaces = placeForTripDtos.stream().map(placeForTripDto -> {
//            placeForTripDto.setTrip(trip);
//            return modelMapper.map(placeForTripDto, Place.class);
//        }).collect(Collectors.toList());
//
//        List<Place> places = trip.getPlaces();
//        for (Place place : newPlaces) {
//            places.add(place);
//        }
//
//        tripRepository.save(trip);
//
//        return modelMapper.map(trip, FullTripDTO.class);
//    }

    @Override
    public FullTripDTO addPlaceToTrip(Long tripId, PlaceDTO placeDTO) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));

        placeDTO.setTripId(tripId);
        placeService.savePlace(placeDTO);

        return modelMapper.map(trip, FullTripDTO.class);
    }

//    @Override
//    public FullTripDTO addPlaceToTrip(Long tripId, PlaceForTripDTO placeForTripDto) {
//        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
//        Place newPlace = modelMapper.map(placeForTripDto, Place.class);
//        newPlace.setTrip(trip);
//        trip.getPlaces().add(newPlace);
//        tripRepository.save(trip);
//
//        return modelMapper.map(trip, FullTripDTO.class);
//    }

    @Override
    public FullTripDTO deletePlaceFromTrip(Long tripId, Long placeId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
        trip = tripRepository.save(trip);
        return modelMapper.map(trip, FullTripDTO.class);
    }

//    @Override
//    public FullTripDTO deletePlaceFromTrip(Long tripId, Long placeId) {
//        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
//        List<Place> places = trip.getPlaces().stream().filter(place -> place.getId() != placeId).collect(Collectors.toList());
//        trip.setPlaces(places);
//        tripRepository.save(trip);
//        return modelMapper.map(trip, FullTripDTO.class);
//    }


}
