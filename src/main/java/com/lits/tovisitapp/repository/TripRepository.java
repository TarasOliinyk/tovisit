package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Trip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {

    Optional<Trip> findById(Long id);

    List<Trip> findAll();

    List<Trip> findAllByAccountId(Long accountId);

}
