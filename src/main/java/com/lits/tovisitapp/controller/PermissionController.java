package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.annotation.*;
import com.lits.tovisitapp.dto.PermissionDTO;
import com.lits.tovisitapp.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/permissions")
@Validated
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @IsAdmin
    @PostMapping("/permission")
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody @Valid PermissionDTO permissionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createPermission(permissionDTO));
    }

    @HasReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable (name = "id")
                                                       @Positive (message = "Permission id cannot be negative") Long id) {
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
    public ResponseEntity<PermissionDTO> updatePermission(@RequestBody @Valid PermissionDTO permissionDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.updatePermission(permissionDTO));
    }

    @HasDeletePermission
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePermission(@PathVariable (name = "id")
                                 @Positive (message = "Permission id cannot be negative") Long id) {
        permissionService.deletePermission(id);
    }
}
