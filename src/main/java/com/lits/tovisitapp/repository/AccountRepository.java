package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findOneById(Long accountId);

    Optional<Account> findOneByUsername(String username);

    List<Account> findAll();
}
