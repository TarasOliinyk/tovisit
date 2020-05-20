package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.PermissionDto;

import java.util.List;

public interface PermissionService {
    Long create(PermissionDto permissionDto);

    PermissionDto findPermission(Long permissionId);

    List<PermissionDto> findAll();

    PermissionDto update(PermissionDto permissionDto);

    void deletePermission(Long permissionId);
}
