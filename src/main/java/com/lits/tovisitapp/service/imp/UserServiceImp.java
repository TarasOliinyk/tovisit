package com.lits.tovisitapp.service.imp;

import com.lits.tovisitapp.dto.UserDto;
import com.lits.tovisitapp.model.User;
import com.lits.tovisitapp.repository.UserRepository;
import com.lits.tovisitapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto findByUsername(String name) {
        User user = userRepository.findByUsername(name).orElseThrow();
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(a -> modelMapper.map(a, UserDto.class)).collect(toList());
    }

    @Override
    public Long create(UserDto userDto) {
        var account = modelMapper.map(userDto, User.class);
        account.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        return userRepository.save(account).getId();
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
