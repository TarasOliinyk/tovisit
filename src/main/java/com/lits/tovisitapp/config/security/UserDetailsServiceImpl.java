package com.lits.tovisitapp.config.security;

import com.lits.tovisitapp.dto.AccountDTO;
import com.lits.tovisitapp.service.AccountService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountService accountService;

    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountDTO accountDTO = accountService.getAccountByUsername(username);
        List<SimpleGrantedAuthority> userAuthorities = accountService.getAccountAuthorities(accountDTO.getId());
        return new User(accountDTO.getUsername(), accountDTO.getPassword(), userAuthorities);
    }
}
