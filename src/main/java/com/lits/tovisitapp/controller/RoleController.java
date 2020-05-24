package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.annotation.*;
import com.lits.tovisitapp.dto.PermissionDTO;
import com.lits.tovisitapp.dto.RoleDTO;
import com.lits.tovisitapp.service.PermissionService;
import com.lits.tovisitapp.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @IsAdmin
    @PostMapping("/role")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleDTO));
    }

    @HasReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable (name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(roleService.getRoleById(id));
    }

    @HasReadPermission
    @GetMapping("/list")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @HasUpdatePermission
    @PutMapping("/role")
    public ResponseEntity<RoleDTO> updateRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleDTO));
    }

    @HasUpdatePermission
    @PutMapping("/{id}/addPermissions")
    public ResponseEntity<RoleDTO> addPermissionsToRole(@PathVariable (name = "id") Long roleId,
                                                        @RequestBody List<Long> permissionIds) {
        RoleDTO roleDTO = roleService.getRoleById(roleId);
        List<PermissionDTO> permissionDTOs = permissionIds.stream().map(permissionService::getPermissionById)
                .collect(Collectors.toList());
                roleDTO.getPermissions().addAll(permissionDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleDTO));
    }

    @HasUpdatePermission
    @PutMapping("/{id}/removePermissions")
    public ResponseEntity<RoleDTO> removePermissionsFromRole(@PathVariable (name = "id") Long roleId,
                                                             @RequestBody List<Long> permissionIds) {
        RoleDTO roleDTO = roleService.getRoleById(roleId);
        List<PermissionDTO> permissionDTOs = permissionIds.stream().map(permissionService::getPermissionById)
                .collect(Collectors.toList());
        roleDTO.getPermissions().removeAll(permissionDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleDTO));
    }

    @HasDeletePermission
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteRole(@PathVariable (name = "id") Long id) {
        roleService.deleteRole(id);
    }
}
