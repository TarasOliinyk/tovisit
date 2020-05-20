package com.lits.tovisitapp.service.imp;

import com.lits.tovisitapp.dto.PermissionDto;
import com.lits.tovisitapp.model.Permission;
import com.lits.tovisitapp.repository.PermissionRepository;
import com.lits.tovisitapp.service.PermissionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long create(PermissionDto permissionDto) {
        return permissionRepository.save(modelMapper.map(permissionDto, Permission.class)).getId();
    }

    @Override
    public PermissionDto findPermission(Long permissionId) {
        Permission permission = permissionRepository.findOneById(permissionId).orElseThrow();
        return modelMapper.map(permission, PermissionDto.class);
    }

    @Override
    public List<PermissionDto> findAll() {
        List<Permission> permissions = (List<Permission>) permissionRepository.findAll();
        return permissions.stream().map(p -> modelMapper.map(p, PermissionDto.class)).collect(toList());
    }

    @Override
    public PermissionDto update(PermissionDto permissionDto) {
        Permission permission = modelMapper.map(permissionDto, Permission.class);
        return modelMapper.map(permissionRepository.save(permission), PermissionDto.class);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}
