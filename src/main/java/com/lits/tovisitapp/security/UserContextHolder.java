package com.lits.tovisitapp.security;

import org.hibernate.usertype.UserType;

public class UserContextHolder {

    private static final ThreadLocal<Long> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<UserType> ROLES = new ThreadLocal<>();

    public static void setUserId(long userId) {
        CONTEXT.set(userId);
    }
    public static long getUserId() {
        return CONTEXT.get();
    }
    public static void clear() {
        CONTEXT.remove();
        ROLES.remove();
    }
    public static void setRole(UserType role) {
        ROLES.set(role);
    }
    public static UserType getRole() {
        return ROLES.get();
    }
}
