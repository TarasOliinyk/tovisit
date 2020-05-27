package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String name);

    List<UserDto> findAll();

    Long create(UserDto userDto);

    UserDto findById(Long id);

    UserDto update(UserDto userDto);

    void delete(Long id);

}
