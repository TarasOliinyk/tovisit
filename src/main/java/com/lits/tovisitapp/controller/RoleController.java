package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.annotation.*;
import com.lits.tovisitapp.dto.RoleDTO;
import com.lits.tovisitapp.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/roles")
@Validated
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @IsAdmin
    @PostMapping("/role")
    public ResponseEntity<RoleDTO> createRole(@RequestBody @Valid RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleDTO));
    }

    @HasReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable (name = "id")
                                           @Positive (message = "Role id cannot negative") Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(roleService.getRoleById(id));
    }

    @HasReadPermission
    @GetMapping("/list")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @HasUpdatePermission
    @PutMapping("/role")
    public ResponseEntity<RoleDTO> updateRole(@RequestBody @Valid RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleDTO));
    }

    @HasUpdatePermission
    @PutMapping("/{id}/addPermissions")
    public ResponseEntity<RoleDTO> addPermissionsToRole(@PathVariable (name = "id")
                                                        @Positive (message = "Role id cannot be negative") Long roleId,
                                                        @RequestBody List<Long> permissionIds) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.addPermissionsToRole(roleId, permissionIds));
    }

    @HasUpdatePermission
    @PutMapping("/{id}/removePermissions")
    public ResponseEntity<RoleDTO> removePermissionsFromRole(@PathVariable (name = "id")
                                                             @Positive (message = "Role id cannot be negative") Long roleId,
                                                             @RequestBody List<Long> permissionIds) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.removePermissionsFromRole(roleId, permissionIds));
    }

    @HasDeletePermission
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteRole(@PathVariable (name = "id") @Positive (message = "Role id cannot be negative") Long id) {
        roleService.deleteRole(id);
    }
}
