package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Long> {

    Optional<Place> findById(Long id);

}
