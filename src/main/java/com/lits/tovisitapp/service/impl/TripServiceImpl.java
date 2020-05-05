package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.repository.TripRepository;
import com.lits.tovisitapp.service.TripService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    private final ModelMapper modelMapper;

    public TripServiceImpl(TripRepository tripRepository, ModelMapper modelMapper) {
        this.tripRepository = tripRepository;
        this.modelMapper = modelMapper;
    }

    // ToDo.
}
