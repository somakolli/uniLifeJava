package de.uniReddit.uniReddit.security;

/**
 * Created by Sokol on 26.11.2017.
 */
public class SecurityConstants {
    static final long EXPIRATION_TIME = 864_000_000;
    static final String SECRET  = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String SIGN_UP_URL = "/api/users/sign-up";
    static final String LOGIN_URL = "/api/login";

}
