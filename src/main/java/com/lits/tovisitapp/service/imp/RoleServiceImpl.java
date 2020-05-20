package com.lits.tovisitapp.service.imp;

import com.lits.tovisitapp.dto.RoleDto;
import com.lits.tovisitapp.model.Role;
import com.lits.tovisitapp.repository.RoleRepository;
import com.lits.tovisitapp.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long create(RoleDto roleDto) {
        return roleRepository.save(modelMapper.map(roleDto, Role.class)).getId();
    }

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(r -> modelMapper.map(r, RoleDto.class)).collect(toList());
    }

    @Override
    public RoleDto findRoleById(Long roleId) {
        Role role = roleRepository.findOneById(roleId).orElseThrow();
        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    public List<RoleDto> findRolesWithIds(List<Long> roleIds) {
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);
        return roleIds.stream().map(r -> modelMapper.map(r, RoleDto.class)).collect(toList());
    }

    @Override
    public RoleDto update(RoleDto roleDto) {
        Role role = modelMapper.map(roleDto, Role.class);
        return modelMapper.map(roleRepository.save(role), RoleDto.class);
    }

    @Override
    public void delete(Long roleId) {
        roleRepository.deleteById(roleId);
    }
}
