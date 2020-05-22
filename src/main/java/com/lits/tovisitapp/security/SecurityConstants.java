package com.lits.tovisitapp.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String ACCOUNT_ID_PARAM = "accountId";
    //public static final String ACCOUNT_ROLE_PARAM = "accountRole";
    public static final String SIGN_UP_URL = "/app/accounts/sign-up";

    private SecurityConstants() {
    }
}
