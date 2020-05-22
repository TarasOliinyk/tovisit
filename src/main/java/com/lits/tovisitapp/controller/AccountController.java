package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.AccountDto;
import com.lits.tovisitapp.service.AccountService;
import com.lits.tovisitapp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public AccountDto assignRoleToAccount(@PathVariable Long id, String name) {
        return accountService.assignRoleToAccount(id, name);
    }

    @DeleteMapping("/account/role/delete/{id}")
    public AccountDto unssignRole(@PathVariable Long id, String name) {
        return accountService.unassignRoleFromAccount(id, name);
    }

    @DeleteMapping("/account/delete/{id}")
    public void delete(@PathVariable Long id){
        accountService.delete(id);
    }
}
