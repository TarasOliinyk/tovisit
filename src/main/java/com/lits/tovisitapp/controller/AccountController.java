package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.AccountDto;
import com.lits.tovisitapp.dto.RoleDto;
import com.lits.tovisitapp.service.AccountService;
import com.lits.tovisitapp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Long singUp(@RequestBody AccountDto accountDto ) {
        return accountService.create(accountDto);
    }

    @GetMapping("/account/{id}")
    public AccountDto findByID(@PathVariable Long id){
        return accountService.findById(id);
    }

    @GetMapping("/account/all")
    public List<AccountDto> findAll(){
        return accountService.findAll();
    }

    @PutMapping("/account/update/{id}")
    public AccountDto update (@RequestBody AccountDto accountDto){
        return accountService.update(accountDto);
    }

    @PutMapping("/account/role/{id}")
    public AccountDto assignRole(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        List<RoleDto> roleDto = roleService
                .findRolesWithIds(roleIds)
                .stream()
                .peek(roleDTO -> roleDTO.setUserId(id))
                .map(roleService::update)
                .collect(Collectors.toList());
        AccountDto accountDto = accountService.findById(id);
        accountDto.setRole(roleDto);
        return accountService.update(accountDto);
    }

    @DeleteMapping("/account/role/delete/{id}")
    public void unAssignRole(@PathVariable Long id, @RequestBody Long roleIds) {
    }

    @DeleteMapping("/account/delete/{id}")
    public void delete(@PathVariable Long id){
        accountService.delete(id);
    }
}
