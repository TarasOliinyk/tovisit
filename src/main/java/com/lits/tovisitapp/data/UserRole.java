package com.lits.tovisitapp.data;

public enum UserRole {
    ROLE_USER,
    ROLE_ADMIN;

    public interface Name {
        String USER = "ROLE_USER";
        String ADMIN = "ROLE_ADMIN";
    }
}
