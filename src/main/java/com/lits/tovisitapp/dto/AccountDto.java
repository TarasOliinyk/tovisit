package com.lits.tovisitapp.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    public List<RoleDto> role = new ArrayList<>();
}
