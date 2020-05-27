package com.lits.tovisitapp.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lits.tovisitapp.context.UserContextHolder;
import com.lits.tovisitapp.data.UserRole;
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
import java.util.Collections;
import java.util.List;

import static com.lits.tovisitapp.config.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
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
            Long userId = decodedJWT.getClaim(USER_ID_PARAM).asLong();
            String role = decodedJWT.getClaim(USER_ROLE_PARAM).asString();
            UserContextHolder.setUserId(userId);
            UserContextHolder.setUserRole(UserRole.valueOf(role));
            List<SimpleGrantedAuthority> userAuthorities = Collections.singletonList(new SimpleGrantedAuthority(role));
            return null != username ? new UsernamePasswordAuthenticationToken(username, userId, userAuthorities) : null;
        }
        return null;
    }
}
