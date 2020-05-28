package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
