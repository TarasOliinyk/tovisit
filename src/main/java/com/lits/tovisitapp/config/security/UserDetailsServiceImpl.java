package com.lits.tovisitapp.config.security;

import com.lits.tovisitapp.dto.UserDTO;
import com.lits.tovisitapp.exceptions.user.UserNotFoundException;
import com.lits.tovisitapp.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDTO userDTO = userService.getUserByUsername(username);
            List<SimpleGrantedAuthority> userAuthorities =
                    Collections.singletonList(new SimpleGrantedAuthority(userService.getUserRole(userDTO.getId()).name()));
            return new User(userDTO.getUsername(), userDTO.getPassword(), userAuthorities);
        } catch (UserNotFoundException e) {
            // Serhiy: throwing proper security exception here which was designed for it, and won't screw up console output
            throw new UsernameNotFoundException("Wrong username or password", e);
        }
    }
}
