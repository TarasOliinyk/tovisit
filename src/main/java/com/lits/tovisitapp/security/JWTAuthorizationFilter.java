package com.lits.tovisitapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import java.util.stream.Collectors;

import static com.lits.tovisitapp.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        var header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        var authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        var token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String username = decodedJWT.getSubject();
            Long userId = decodedJWT.getClaim(ACCOUNT_ID_PARAM).asLong();

            List<SimpleGrantedAuthority> userRole = decodedJWT.getClaim(ACCOUNT_ROLE_PARAM).asList(String.class)
                    .stream().map(p -> new SimpleGrantedAuthority(p)).collect(Collectors.toList());
            if (username != null) {
                return new UsernamePasswordAuthenticationToken(
                        username,
                        userId,
                        userRole);
            }
            return null;
        }
        return null;
    }
}
