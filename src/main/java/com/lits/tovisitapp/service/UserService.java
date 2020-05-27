package com.lits.tovisitapp.service;

import com.lits.tovisitapp.data.UserRole;
import com.lits.tovisitapp.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers();

    UserDTO getUserByUsername(String username);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO assignRole(Long userId, UserRole role);

    UserRole getUserRole(Long userId);

    void deleteUser(Long userId);
}
