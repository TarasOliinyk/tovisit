package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.PermissionDTO;
import com.lits.tovisitapp.dto.RoleDTO;
import com.lits.tovisitapp.exceptions.role.RoleNotFoundException;
import com.lits.tovisitapp.model.Role;
import com.lits.tovisitapp.repository.PermissionRepository;
import com.lits.tovisitapp.repository.RoleRepository;
import com.lits.tovisitapp.service.RoleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository,
                           ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        logger.info("Crate role " + roleDTO.toString());
        Role role = modelMapper.map(roleDTO, Role.class);
        return modelMapper.map(roleRepository.save(role), RoleDTO.class);
    }

    @Override
    public RoleDTO getRoleById(Long roleId) {
        logger.info("Get role with id " + roleId);
        return modelMapper.map(roleRepository.findOneById(roleId).orElseThrow(
                () -> new RoleNotFoundException("There is no role with id " + roleId)), RoleDTO.class);
    }

    @Override
    public RoleDTO getRoleByName(String roleName) {
        logger.info("Get role with name " + roleName);
        return modelMapper.map(roleRepository.findOneByName(roleName).orElseThrow(
                () -> new RoleNotFoundException("There is no role with name " + roleName)), RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        logger.info("Get all roles");
        Type listType = new TypeToken<List<RoleDTO>>(){}.getType();
        return modelMapper.map(roleRepository.findAll(), listType);
    }

    @Override
    public List<RoleDTO> getRolesWithIds(List<Long> roleIds) {
        logger.info("Get all roles with ids " + roleIds.toString());
        Type listType = new TypeToken<List<RoleDTO>>(){}.getType();
        return modelMapper.map(roleRepository.findAllByIdIn(roleIds), listType);
    }

    @Override
    public RoleDTO updateRole(RoleDTO roleDTO) {
        logger.info(String.format("Update role with id %s, updated role: %s", roleDTO.getId(), roleDTO.toString()));
        Role role = modelMapper.map(roleDTO, Role.class);
        return modelMapper.map(roleRepository.save(role), RoleDTO.class);
    }

    private RoleDTO manipulateRolePermissions(Long roleId, List<Long> permissionIds, boolean add) {
        RoleDTO roleDTO = getRoleById(roleId);
        Type listType = new TypeToken<List<PermissionDTO>>(){}.getType();
        List<PermissionDTO> permissionDTOs = modelMapper.map(permissionRepository.findAllByIdIn(permissionIds), listType);

        if (add) {
            roleDTO.getPermissions().addAll(permissionDTOs);
        } else {
            roleDTO.getPermissions().removeAll(permissionDTOs);
        }
        return updateRole(roleDTO);
    }

    @Override
    public RoleDTO addPermissionsToRole(Long roleId, List<Long> permissionIds) {
        logger.info(String.format("Add permissions with ids %s to role with id %s", permissionIds.toString(), roleId));
        return manipulateRolePermissions(roleId, permissionIds, true);
    }

    @Override
    public RoleDTO removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        logger.info(String.format("Remove permissions with ids %s from role with id %s", permissionIds.toString(), roleId));
        return manipulateRolePermissions(roleId, permissionIds, false);
    }

    @Override
    public void deleteRole(Long roleId) {
        logger.info("Delete role with id " + roleId);
        roleRepository.deleteById(roleId);
    }
}
