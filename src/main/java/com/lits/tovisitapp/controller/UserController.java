package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.UserDto;
import com.lits.tovisitapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Long singUp(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @GetMapping("/user/{id}")
    public UserDto findByID(@PathVariable Long id){
        return userService.findById(id);
    }

    @GetMapping("/user/all")
    public List<UserDto> findAll(){
        return userService.findAll();
    }

    @PutMapping("/user/{id}")
    public UserDto update (@RequestBody UserDto userDto){
        return userService.update(userDto);
    }


    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }
}
