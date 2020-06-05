package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.user.UserCreateDTO;
import com.lits.tovisitapp.dto.user.UserDTO;
import com.lits.tovisitapp.dto.user.role.UserRoleDTO;

import java.util.List;

public interface UserService {

    UserCreateDTO createUser(UserCreateDTO userDTO);

    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers();

    UserDTO getUserByUsername(String username);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO assignRole(Long userId, UserRoleDTO roleDTO);

    UserRoleDTO getUserRole(Long userId);

    void deleteUser(Long userId);
}
