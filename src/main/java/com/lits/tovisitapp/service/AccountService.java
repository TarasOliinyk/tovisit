package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto findByUsername(String name);

    List<AccountDto> findAll();

    Long create(AccountDto accountDto);

    AccountDto findById(Long id);

    AccountDto update(AccountDto accountDto);

    void delete(Long id);
}
