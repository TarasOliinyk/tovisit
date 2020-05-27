package com.lits.tovisitapp.config.security;

public interface SecurityConstants {
    String SECRET = "SecretKeyToGenJWTs";
    long EXPIRATION_TIME = 864_000_000; // 10 days
    String TOKEN_PREFIX = "Bearer ";
    String HEADER = "Authorization";
    String USER_ID_PARAM = "userId";
    String USER_ROLE_PARAM = "userRole";
    String SIGN_UP_URL = "/users/sign-up";
    String[] SWAGGER_AUTH_WHITELIST = {
            // Swagger UI
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
    };
}
