package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.repository.TypeRepository;
import com.lits.tovisitapp.service.TypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;
    private final ModelMapper modelMapper;

    public TypeServiceImpl(TypeRepository typeRepository, ModelMapper modelMapper) {
        this.typeRepository = typeRepository;
        this.modelMapper = modelMapper;
    }

    // ToDo.
}
