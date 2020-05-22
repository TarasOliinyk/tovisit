package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.RoleDto;
import com.lits.tovisitapp.service.PermissionService;
import com.lits.tovisitapp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/role")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody RoleDto roleDto) {
        return roleService.create(roleDto);
    }

    @GetMapping("/role/{id}")
    public List<RoleDto> findRolesWithIds(@PathVariable List<Long> id) {
        return roleService.findRolesWithIds(id);
    }

    @GetMapping("/role/all")
    public List<RoleDto> findAll() {
        return roleService.findAll();
    }


    @PutMapping("/role/perm/{id}")
    public RoleDto assignPermissionsToRole(@PathVariable String role, String permission) {
        return roleService.assignPermissionToRole(role, permission);
    }

    @DeleteMapping("/role/perm/delete/{id}")
    public RoleDto unassignPermissionFromRole(@PathVariable String role, String permission) {
        return roleService.unassignPermissionFromRole(role, permission);
    }

    @DeleteMapping("/role/delete/{id}")
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }
}
