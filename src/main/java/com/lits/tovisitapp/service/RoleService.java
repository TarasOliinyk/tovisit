package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO getRoleById(Long roleId);

    RoleDTO getRoleByName(String roleName);

    List<RoleDTO> getAllRoles();

    List<RoleDTO> getRolesWithIds(List<Long> roleIds);

    RoleDTO updateRole(RoleDTO roleDTO);

    void deleteRole(Long roleId);
}
