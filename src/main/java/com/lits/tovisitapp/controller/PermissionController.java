package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.PermissionDto;
import com.lits.tovisitapp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/permission")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody PermissionDto permissionDto) {
        return permissionService.create(permissionDto);
    }

    @GetMapping("/permission/{id}")
    public PermissionDto findPermission(@PathVariable Long id) {
        return permissionService.findPermission(id);
    }

    @GetMapping("/permission/all")
    public List<PermissionDto> findAllPermissions() {
        return permissionService.findAll();
    }


    @DeleteMapping("/permission/delete/{id}")
    public void deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
    }
}
