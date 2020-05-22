package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.AccountDto;
import com.lits.tovisitapp.dto.PermissionDto;
import com.lits.tovisitapp.dto.RoleDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface AccountService {

    AccountDto findByUsername(String name);

    List<AccountDto> findAll();

    List<SimpleGrantedAuthority> getUserAuthorities(Long id);

    Long create(AccountDto accountDto);

    AccountDto findById(Long id);

    AccountDto update(AccountDto accountDto);

    void delete(Long id);

    AccountDto assignRoleToAccount(Long id, String name);

    AccountDto unassignRoleFromAccount(Long id, String name);

}
