package com.lits.tovisitapp.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lits.tovisitapp.dto.AccountDto;
import com.lits.tovisitapp.model.Permission;
import com.lits.tovisitapp.model.Role;
import com.lits.tovisitapp.repository.AccountRepository;
import com.lits.tovisitapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.lits.tovisitapp.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountRepository accountRepository;

    public JWTAuthenticationFilter(AccountService accountService, AuthenticationManager authenticationManager) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            AccountDto accountDto = new ObjectMapper().readValue(request.getInputStream(), AccountDto.class);
            com.lits.tovisitapp.model.Account account = accountRepository.findByUsername(accountDto.getUsername()).orElseThrow();
            List<SimpleGrantedAuthority> permissions = account.getRole().stream().map(Role::getPermissions)
                    .flatMap(Collection::stream).map(Permission::getName).map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    accountDto.getUsername(),
                    accountDto.getPassword(),
                    permissions));
        } catch (Exception e) {

            if (e instanceof BadCredentialsException) {
                throw new com.lits.tovisitapp.exception.BadCredentialsException("Invalid login or password");
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) {
        User user = (User) auth.getPrincipal();
        AccountDto accountDto = accountService.findByUsername(user.getUsername());
        accountDto.setPassword(null);
        com.lits.tovisitapp.model.Account account = accountRepository.findByUsername(accountDto.getUsername()).orElseThrow();
        List<String> permissions = account.getRole().stream().map(Role::getPermissions).flatMap(Collection::stream)
                .map(Permission::getName).collect(Collectors.toList());

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim(ACCOUNT_ID_PARAM, accountDto.getId())
                .withClaim(ACCOUNT_ROLE_PARAM, permissions)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        response.setContentType("application/json");
    }
}
