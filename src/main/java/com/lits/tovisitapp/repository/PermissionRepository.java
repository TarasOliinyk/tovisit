package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
    Optional<Permission> findOneById(Long id);
    Optional<Permission> findOneByName(String name);
}
