package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.dto.TripDTO;
import com.lits.tovisitapp.dto.TripPlaceDTO;
import com.lits.tovisitapp.exceptions.place.PlaceNotFoundException;
import com.lits.tovisitapp.exceptions.trip.TripNotFoundException;
import com.lits.tovisitapp.exceptions.user.UserNotFoundException;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.model.User;
import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.repository.TripRepository;
import com.lits.tovisitapp.repository.UserRepository;
import com.lits.tovisitapp.service.PlaceService;
import com.lits.tovisitapp.service.TripService;
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
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final PlaceService placeService;

    public TripServiceImpl(ModelMapper modelMapper, TripRepository tripRepository, UserRepository userRepository, PlaceRepository placeRepository, PlaceService placeService) {
        this.modelMapper = modelMapper;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.placeService = placeService;
    }

    @Override
    public TripDTO create(TripDTO tripDto) {
        User user = userRepository.findOneById(tripDto.getUserId()).orElseThrow(() -> new UserNotFoundException(format("User with id = %d doesn't exist", tripDto.getUserId())));
        Trip trip = modelMapper.map(tripDto, Trip.class);
        return modelMapper.map(tripRepository.save(trip), TripDTO.class);
    }

    @Override
    public TripDTO update(TripDTO newTripDTO) {
        Trip trip = tripRepository.findById(newTripDTO.getId()).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", newTripDTO.getId())));
        User user = userRepository.findOneById(newTripDTO.getUserId()).orElseThrow(() -> new UserNotFoundException(format("User with id = %d doesn't exist", newTripDTO.getUserId())));
        Trip newTrip = modelMapper.map(newTripDTO, Trip.class);
        return modelMapper.map(tripRepository.save(newTrip), TripDTO.class);
    }

    @Override
    public void delete(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", id)));
        tripRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TripPlaceDTO getOne(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", id)));
        return modelMapper.map(trip, TripPlaceDTO.class);
    }

    @Override
    public List<TripDTO> getAll() {
        List<Trip> trips = tripRepository.findAll();
        List<TripDTO> tripDtos = trips.stream().map(trip -> modelMapper.map(trip, TripDTO.class)).collect(Collectors.toList());
        return tripDtos;
    }

    @Override
    public List<TripDTO> getAllByUserId(Long userId) {
        User account = userRepository.findOneById(userId).orElseThrow(() -> new UserNotFoundException(format("User with id = %d doesn't exist", userId)));
        List<Trip> trips = tripRepository.findAllByUserId(userId);
        List<TripDTO> tripDtos = trips.stream().map(trip -> modelMapper.map(trip, TripDTO.class)).collect(Collectors.toList());
        return tripDtos;
    }

    @Override
    public TripPlaceDTO addPlacesToTrip(Long tripId, List<PlaceDTO> placeDtos) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
        for (PlaceDTO placeDto : placeDtos) {
            placeDto.setTripId(tripId);
            placeService.savePlace(placeDto);
        }
        return modelMapper.map(trip, TripPlaceDTO.class);
    }

    @Override
    @Transactional
    public TripPlaceDTO deletePlaceFromTrip(Long tripId, Long placeId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
        Place placeForDeleting = placeRepository.findById(placeId).orElseThrow(() -> new PlaceNotFoundException(format("Place with id = %d doesn't exist", placeId)));
        if (trip.getPlaces().contains(placeForDeleting)) {
            trip.getPlaces().remove(placeForDeleting);
            tripRepository.save(trip);
            return modelMapper.map(trip, TripPlaceDTO.class);
        } else
            throw new PlaceNotFoundException(format("Place with id = %d doesn't exist at trip with id = %d", placeId, tripId));
    }

}
