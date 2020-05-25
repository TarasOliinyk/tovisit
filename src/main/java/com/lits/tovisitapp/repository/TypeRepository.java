package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
	List<Type> findByNameIn(List<String> names);
}
