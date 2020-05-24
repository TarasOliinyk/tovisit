package com.lits.tovisitapp.data;

public enum PermissionLevel {
    CREATE,
    READ,
    UPDATE,
    DELETE;

    public interface Name {
        String CREATE = "CREATE";
        String READ = "READ";
        String UPDATE = "UPDATE";
        String DELETE = "DELETE";
    }
}
