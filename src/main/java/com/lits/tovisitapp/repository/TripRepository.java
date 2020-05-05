package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Trip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {

    // ToDo.
}
