package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Long> {

    // ToDo.
}
