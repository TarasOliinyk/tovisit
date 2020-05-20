package com.lits.tovisitapp.dto;

import lombok.Data;

@Data
public class PermissionDto {
    private Long id;

    private String name;
    private Long roleId;

    public PermissionDto(String name, Long roleId) {
        this.name = name;
        this.roleId = roleId;
    }
}