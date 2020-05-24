package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.annotation.*;
import com.lits.tovisitapp.dto.AccountDTO;
import com.lits.tovisitapp.dto.RoleDTO;
import com.lits.tovisitapp.service.AccountService;
import com.lits.tovisitapp.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final RoleService roleService;;

    public AccountController(AccountService accountService, RoleService roleService) {
        this.accountService = accountService;
        this.roleService = roleService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountDTO> singUp(@RequestBody AccountDTO accountDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDTO));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody AccountDTO accountDTO) {
        // Login processing (JWT token generation) is handled by JWTAuthenticationFilter, this endpoint is created for
        // exposing it to Swagger
    }

    @HasReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable (name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(accountService.getAccountById(id));
    }

    @HasReadPermission
    @GetMapping("/list")
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @HasUpdatePermission
    @PutMapping("/{accountId}/addRoles")
    public ResponseEntity<AccountDTO> assignRoles(@PathVariable (name = "accountId") Long accountId,
                                                  @RequestBody List<Long> roleIds) {
        AccountDTO accountDTO = accountService.getAccountById(accountId);
        List<RoleDTO> roleDTOs = roleService.getRolesWithIds(roleIds);
        accountDTO.getRoles().addAll(roleDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(accountDTO));
    }

    @HasUpdatePermission
    @PutMapping("/{accountId}/removeRole/{roleId}")
    public ResponseEntity<AccountDTO> unassignRole(@PathVariable (name = "accountId") Long accountId,
                                                   @PathVariable (name = "roleId") Long roleId) {
        RoleDTO roleToRemove = roleService.getRoleById(roleId);
        AccountDTO accountDTO = accountService.getAccountById(accountId);
        accountDTO.getRoles().remove(roleToRemove);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(accountDTO));
    }

    @HasDeletePermission
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAccount(@PathVariable (name = "id") Long id) {
        accountService.deleteAccount(id);
    }
}
