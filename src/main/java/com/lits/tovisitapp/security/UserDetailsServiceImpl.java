package com.lits.tovisitapp.security;

import com.lits.tovisitapp.dto.AccountDto;
import com.lits.tovisitapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Autowired
    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        AccountDto login = accountService.findByUsername(name);
        return new User(login.getUsername(), login.getPassword(),
                Collections.emptyList());
    }
}
