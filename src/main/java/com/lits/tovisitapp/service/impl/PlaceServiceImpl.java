package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.service.PlaceService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final ModelMapper modelMapper;

    public PlaceServiceImpl(PlaceRepository placeRepository, ModelMapper modelMapper) {
        this.placeRepository = placeRepository;
        this.modelMapper = modelMapper;
    }

    // ToDo.
}
