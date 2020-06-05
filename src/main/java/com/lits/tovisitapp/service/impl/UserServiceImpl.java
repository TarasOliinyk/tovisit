package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.user.UserCreateDTO;
import com.lits.tovisitapp.dto.user.UserDTO;
import com.lits.tovisitapp.dto.user.role.UserRoleDTO;
import com.lits.tovisitapp.exceptions.user.UserNotFoundException;
import com.lits.tovisitapp.model.User;
import com.lits.tovisitapp.repository.UserRepository;
import com.lits.tovisitapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserCreateDTO createUser(UserCreateDTO userCreateDTO) {
        logger.info("Create user with username " + userCreateDTO.getUsername());
        User user = modelMapper.map(userCreateDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userCreateDTO.getPassword()));
        return modelMapper.map(userRepository.save(user), UserCreateDTO.class);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        logger.info("Get user with id " + userId);
        return modelMapper.map(userRepository.findOneById(userId).orElseThrow(
                () -> new UserNotFoundException("There is no user with id " + userId)), UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        logger.info("Get all users");
        return modelMapper.map(userRepository.findAll(), new TypeToken<List<UserDTO>>(){}.getType());
    }

    @Override
    @Transactional
    public UserDTO getUserByUsername(String username) {
        logger.info("Get user with username " + username);
        return modelMapper.map(userRepository.findOneByUsername(username).orElseThrow(
                () -> new UserNotFoundException("There is no user with username " + username)), UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        Long userId = userDTO.getId();
        logger.info(String.format("Update user with id %s, new user data: %s", userId, userDTO.toString()));
        User user = userRepository.findOneById(userId).orElse(null);

        if (null != user) {
            user = modelMapper.map(userDTO, User.class);
            return modelMapper.map(userRepository.save(user), UserDTO.class);
        } else {
            throw new UserNotFoundException("User cannot be updated, there is no user with id " + userId);
        }
    }

    @Override
    public UserDTO assignRole(Long userId, UserRoleDTO roleDTO) {
        logger.info(String.format("Assign role '%s' to user with id %s", roleDTO.getRole(), userId));
        User user = modelMapper.map(getUserById(userId), User.class);
        user.setRole(roleDTO.getRole());
        userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserRoleDTO getUserRole(Long userId) {
        logger.info("Get role of user with id " + userId);
        User user = userRepository.findOneById(userId).orElseThrow(
                () -> new UserNotFoundException("There is no user with id " + userId));
        return new UserRoleDTO(user.getRole());
    }

    @Override
    public void deleteUser(Long userId) {
        logger.info("Delete user with id " + userId);
        User user = userRepository.findOneById(userId).orElse(null);

        if (null != user) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("User cannot be deleted, there is no user with id " + userId);
        }
    }
}
