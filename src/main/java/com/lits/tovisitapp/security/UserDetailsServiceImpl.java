package com.lits.tovisitapp.security;

import com.lits.tovisitapp.dto.UserDto;
import com.lits.tovisitapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        UserDto userDto = userService.findByUsername(username);
        return new User(userDto.getUsername(), userDto.getPassword(), Collections.emptyList());
    }
}
