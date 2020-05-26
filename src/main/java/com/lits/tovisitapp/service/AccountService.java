package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.AccountDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccountById(Long accountId);

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountByUsername(String username);

    AccountDTO updateAccount(AccountDTO account);

    AccountDTO assignRoles(Long accountId, List<Long> roleIds);

    AccountDTO unassignRole(Long accountId, Long roleId);

    List<SimpleGrantedAuthority> getAccountAuthorities(Long accountId);

    void deleteAccount(Long accountId);
}
