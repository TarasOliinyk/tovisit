package com.lits.tovisitapp.context;

import com.lits.tovisitapp.data.UserRole;

public class UserContextHolder {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<UserRole> USER_ROLE = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void setUserRole(UserRole role) {
        USER_ROLE.set(role);
    }

    public static UserRole getUserRole() {
        return USER_ROLE.get();
    }

    public static void clearContext() {
        USER_ID.remove();
        USER_ROLE.remove();
    }
}
