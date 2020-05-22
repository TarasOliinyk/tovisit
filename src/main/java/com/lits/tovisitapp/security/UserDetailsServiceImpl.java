package com.lits.tovisitapp.security;

import com.lits.tovisitapp.dto.AccountDto;
import com.lits.tovisitapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Autowired
    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        AccountDto accountDto = accountService.findByUsername(username);
        List<SimpleGrantedAuthority> authorities = accountService.getUserAuthorities(accountDto.getId());
        return new User(accountDto.getUsername(), accountDto.getPassword(), authorities);
    }
}
