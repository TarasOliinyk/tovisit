package com.lits.tovisitapp.repository;

import com.lits.tovisitapp.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    // ToDo.
}
