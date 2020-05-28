package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.CreatePlaceDto;
import com.lits.tovisitapp.dto.TripDto;
import com.lits.tovisitapp.dto.TripPlaceDto;
import com.lits.tovisitapp.exceptions.AccountNotFoundException;
import com.lits.tovisitapp.exceptions.PlaceNotFoundException;
import com.lits.tovisitapp.exceptions.TripNotFoundException;
import com.lits.tovisitapp.model.Account;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.repository.AccountRepository;
import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.repository.TripRepository;
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
    private final AccountRepository accountRepository;
    private final PlaceRepository placeRepository;

    public TripServiceImpl(ModelMapper modelMapper, TripRepository tripRepository, AccountRepository accountRepository, PlaceRepository placeRepository) {
        this.modelMapper = modelMapper;
        this.tripRepository = tripRepository;
        this.accountRepository = accountRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public TripDto create(TripDto tripDto) {
        Account account = accountRepository.findById(tripDto.getAccountId()).orElseThrow(() -> new AccountNotFoundException(format("Account with id = %d doesn't exist", tripDto.getAccountId())));
        Trip trip = modelMapper.map(tripDto, Trip.class);
        return modelMapper.map(tripRepository.save(trip), TripDto.class);
    }

    @Override
    public TripDto update(TripDto newTripDto) {
        Trip trip = tripRepository.findById(newTripDto.getId()).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", newTripDto.getId())));
        Account account = accountRepository.findById(newTripDto.getAccountId()).orElseThrow(() -> new AccountNotFoundException(format("Account with id = %d doesn't exist", newTripDto.getAccountId())));
        Trip newTrip = modelMapper.map(newTripDto, Trip.class);
        return modelMapper.map(tripRepository.save(newTrip), TripDto.class);
    }

    @Override
    public void delete(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", id)));
        tripRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TripPlaceDto getOne(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", id)));
        return modelMapper.map(trip, TripPlaceDto.class);
    }

    @Override
    public List<TripDto> getAll() {
        List<Trip> trips = tripRepository.findAll();
        List<TripDto> tripDtos = trips.stream().map(trip -> {
//            List<Long> placeIds = trip.getPlaces().stream().map(Place::getId).collect(Collectors.toList());
            TripDto tripDto = modelMapper.map(trip, TripDto.class);
//            shortTripDto.setPlaceIds(placeIds);
            return tripDto;
        }).collect(Collectors.toList());
        return tripDtos;
    }

    @Override
    public List<TripDto> getAllByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(format("Account with id = %d doesn't exist", accountId)));
        List<Trip> trips = tripRepository.findAllByAccountId(accountId);
        List<TripDto> tripDtos = trips.stream().map(trip -> {
//            List<Long> placeIds = trip.getPlaces().stream().map(Place::getId).collect(Collectors.toList());
            TripDto tripDto = modelMapper.map(trip, TripDto.class);
//            shortTripDto.setPlaceIds(placeIds);
            return tripDto;
        }).collect(Collectors.toList());
        return tripDtos;
    }

    @Override
    @Transactional
    public TripPlaceDto addPlacesToTrip(Long tripId, List<CreatePlaceDto> createPlaceDtos) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
        for (CreatePlaceDto createPlaceDto : createPlaceDtos) {
            createPlaceDto.setTrip(trip);
            Place place = modelMapper.map(createPlaceDto, Place.class);
            placeRepository.save(place);
        }
        return modelMapper.map(trip, TripPlaceDto.class);
    }

    @Override
    @Transactional
    public TripPlaceDto deletePlaceFromTrip(Long tripId, Long placeId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException(format("Trip with id = %d doesn't exist", tripId)));
        Place placeForDeleting = placeRepository.findById(placeId).orElseThrow(() -> new PlaceNotFoundException(format("Place with id = %d doesn't exist", placeId)));
        if (trip.getPlaces().contains(placeForDeleting)) {
            trip.getPlaces().remove(placeForDeleting);
            tripRepository.save(trip);
            return modelMapper.map(trip, TripPlaceDto.class);
        } else
            throw new PlaceNotFoundException(format("Place with id = %d doesn't exist at trip with id = %d", placeId, tripId));
    }


}
