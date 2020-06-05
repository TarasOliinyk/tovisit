package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.annotation.role.IsAdmin;
import com.lits.tovisitapp.config.security.UserContextHolder;
import com.lits.tovisitapp.dto.user.UserCreateDTO;
import com.lits.tovisitapp.dto.user.UserDTO;
import com.lits.tovisitapp.dto.user.UserLoginDTO;
import com.lits.tovisitapp.dto.user.role.UserRoleDTO;
import com.lits.tovisitapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController()
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserCreateDTO> singUp(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateDTO));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        // Login processing (JWT token generation) is handled by JWTAuthenticationFilter, this endpoint is created for
        // exposing it to Swagger
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id")
                                           @Positive(message = "User id cannot be negative") Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUserById(id));
    }

    @GetMapping("/list")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @IsAdmin
    @PutMapping("/{userId}/role")
    public ResponseEntity<UserDTO> assignRole(@PathVariable(name = "userId")
                                              @Positive(message = "User id cannot be negative") Long userId,
                                              @RequestBody UserRoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.assignRole(userId, roleDTO));
    }

    @IsAdmin
    @GetMapping("/{userId}/role")
    public ResponseEntity<UserRoleDTO> getUserRole(@PathVariable(name = "userId")
                                                @Positive(message = "User id cannot be negative") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserRole(userId));
    }

    @IsAdmin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable(name = "id") @Positive(message = "User id cannot be negative") Long id) {
        userService.deleteUser(id);
        UserContextHolder.clearContext();
    }
}
