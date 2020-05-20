package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findOneById(Long id);

    List<Role> findAll();

    List<Role> findAllByIdIn(List<Long> ids);
}
