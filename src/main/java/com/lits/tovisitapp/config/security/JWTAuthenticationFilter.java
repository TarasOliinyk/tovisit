package com.lits.tovisitapp.config.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lits.tovisitapp.dto.UserDTO;
import com.lits.tovisitapp.repository.UserRepository;
import com.lits.tovisitapp.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.lits.tovisitapp.config.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;

    JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService,
                                   UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
        this.setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            UserDTO userDTO = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
            String username = userDTO.getUsername();
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,
                    userDTO.getPassword(),
                    new ArrayList<>()
                    ));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new UsernameNotFoundException(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) {
        User user = (User) auth.getPrincipal();
        UserDTO userDTO = userService.getUserByUsername(user.getUsername());
        userDTO.setPassword(null);

        Long userId = userDTO.getId();

        com.lits.tovisitapp.model.User userEntity = userRepository.findOneById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with id " + userId));

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim(USER_ID_PARAM, userDTO.getId())
                .withClaim(USER_ROLE_PARAM, userEntity.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        response.addHeader(HEADER, TOKEN_PREFIX + token);
        response.setContentType("application/json");
    }
}
