package com.lits.tovisitapp.dto.user.role;

import com.lits.tovisitapp.data.UserRole;
import lombok.Data;

@Data
public class UserRoleDTO {

    private UserRole role;

    public UserRoleDTO(UserRole role) {
        this.role = role;
    }
}
