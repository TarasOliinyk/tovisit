package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Type;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends CrudRepository<Type, Long> {

    // ToDo.
}
