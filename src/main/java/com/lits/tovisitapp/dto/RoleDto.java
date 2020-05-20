package com.lits.tovisitapp.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private Long userId;
    private List<PermissionDto> permissions = new ArrayList<>();
}