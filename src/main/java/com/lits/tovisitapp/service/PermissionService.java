package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.PermissionDTO;

import java.util.List;

public interface PermissionService {

    PermissionDTO createPermission(PermissionDTO permissionDTO);

    PermissionDTO getPermissionById(Long permissionId);

    List<PermissionDTO> getAllPermissions();

    PermissionDTO updatePermission(PermissionDTO permissionDTO);

    void deletePermission(Long permissionId);
}
