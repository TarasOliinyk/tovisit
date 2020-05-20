package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.RoleDto;

import java.util.List;

public interface RoleService {
    Long create(RoleDto roleDto);

    List<RoleDto> findAll();

    RoleDto findRoleById(Long roleId);

    List<RoleDto> findRolesWithIds(List<Long> roleIds);

    RoleDto update(RoleDto roleDto);

    void delete(Long roleId);
}