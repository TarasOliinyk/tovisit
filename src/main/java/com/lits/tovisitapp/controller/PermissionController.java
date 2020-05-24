package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.annotation.*;
import com.lits.tovisitapp.dto.PermissionDTO;
import com.lits.tovisitapp.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @IsAdmin
    @PostMapping("/permission")
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createPermission(permissionDTO));
    }

    @HasReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable (name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(permissionService.getPermissionById(id));
    }

    @HasReadPermission
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.FOUND)
    public List<PermissionDTO> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @HasUpdatePermission
    @PutMapping("/permission")
    public ResponseEntity<PermissionDTO> updatePermission(@RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.updatePermission(permissionDTO));
    }

    @HasDeletePermission
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePermission(@PathVariable (name = "id") Long id) {
        permissionService.deletePermission(id);
    }
}
