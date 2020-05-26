package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.annotation.*;
import com.lits.tovisitapp.dto.AccountDTO;
import com.lits.tovisitapp.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController()
@RequestMapping("/accounts")
@Validated
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountDTO> singUp(@RequestBody @Valid AccountDTO accountDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDTO));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody @Valid AccountDTO accountDTO) {
        // Login processing (JWT token generation) is handled by JWTAuthenticationFilter, this endpoint is created for
        // exposing it to Swagger
    }

    @HasReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable (name = "id")
                                                 @Positive (message = "Account id cannot be negative") Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(accountService.getAccountById(id));
    }

    @HasReadPermission
    @GetMapping("/list")
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @HasUpdatePermission
    @PutMapping("/{accountId}/addRoles")
    public ResponseEntity<AccountDTO> assignRoles(@PathVariable (name = "accountId")
                                                  @Positive (message = "Account id cannot be negative") Long accountId,
                                                  @RequestBody List<Long> roleIds) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.assignRoles(accountId, roleIds));
    }

    @HasUpdatePermission
    @PutMapping("/{accountId}/removeRole/{roleId}")
    public ResponseEntity<AccountDTO> unassignRole(@PathVariable (name = "accountId")
                                                   @Positive (message = "Account id cannot be negative") Long accountId,
                                                   @PathVariable (name = "roleId")
                                                   @Positive (message = "Role id cannot be negative") Long roleId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.unassignRole(accountId, roleId));
    }

    @HasDeletePermission
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAccount(@PathVariable (name = "id") @Positive (message = "Account id cannot be negative") Long id) {
        accountService.deleteAccount(id);
    }
}
