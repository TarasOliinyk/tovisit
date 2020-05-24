package com.lits.tovisitapp.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lits.tovisitapp.service.AccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.lits.tovisitapp.config.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final AccountService accountService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AccountService accountService) {
        super(authenticationManager);
        this.accountService = accountService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER);

        if (null == header || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String header = request.getHeader(HEADER);

        if (null != header) {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                    .verify(header.replace(TOKEN_PREFIX, ""));
            String username = decodedJWT.getSubject();
            Long accountId = decodedJWT.getClaim(ACCOUNT_ID_PARAM).asLong();
            List<SimpleGrantedAuthority> accountAuthorities = accountService.getAccountAuthorities(accountId);
            return null != username ? new UsernamePasswordAuthenticationToken(username, accountId, accountAuthorities) : null;
        } else {
            return null;
        }
    }
}
