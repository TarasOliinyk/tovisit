package com.lits.tovisitapp.config.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lits.tovisitapp.dto.AccountDTO;
import com.lits.tovisitapp.service.AccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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
    private final AccountService accountService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.setFilterProcessesUrl("/accounts/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            AccountDTO accountDTO = new ObjectMapper().readValue(request.getInputStream(), AccountDTO.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    accountDTO.getUsername(),
                    accountDTO.getPassword(),
                    new ArrayList<>()));
        } catch (Exception e) {

            if (e instanceof BadCredentialsException) {
                throw new BadCredentialsException("Invalid login or password");
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) {
        User user = (User) auth.getPrincipal();
        AccountDTO accountDTO = accountService.getAccountByUsername(user.getUsername());
        accountDTO.setPassword(null);

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim(ACCOUNT_ID_PARAM, accountDTO.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        response.addHeader(HEADER, TOKEN_PREFIX + token);
        response.setContentType("application/json");
    }
}
