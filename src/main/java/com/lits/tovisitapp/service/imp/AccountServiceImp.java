package com.lits.tovisitapp.service.imp;

import com.lits.tovisitapp.dto.AccountDto;
import com.lits.tovisitapp.dto.PermissionDto;
import com.lits.tovisitapp.dto.RoleDto;
import com.lits.tovisitapp.model.Account;
import com.lits.tovisitapp.model.Role;
import com.lits.tovisitapp.repository.AccountRepository;
import com.lits.tovisitapp.repository.RoleRepository;
import com.lits.tovisitapp.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AccountServiceImp(AccountRepository accountRepository, RoleRepository roleRepository, ModelMapper modelMapper,
                             BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public AccountDto findByUsername(String name) {
        Account account = accountRepository.findByUsername(name).orElseThrow();
        return modelMapper.map(account, AccountDto.class);
    }

    @Override
    public List<AccountDto> findAll() {
        List<Account> accounts = (List<Account>) accountRepository.findAll();
        return accounts.stream().map(a -> modelMapper.map(a, AccountDto.class)).collect(toList());
    }

    @Override
    public List<SimpleGrantedAuthority> getUserAuthorities(Long id) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Account account = accountRepository.findById(id).orElseThrow();
        account.getRole().stream()
                .peek(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getName())))
                .map(Role::getPermissions)
                .flatMap(Collection::stream)
                .forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName())));
        return authorities;
    }

    @Override
    public Long create(AccountDto accountDto) {
        var account = modelMapper.map(accountDto, Account.class);
        account.setPassword(bCryptPasswordEncoder.encode(accountDto.getPassword()));
        return accountRepository.save(account).getId();
    }

    @Override
    public AccountDto findById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow();
        return modelMapper.map(account, AccountDto.class);
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        return modelMapper.map(accountRepository.save(account), AccountDto.class);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public AccountDto assignRoleToAccount(Long id, String name) {
        Account account = accountRepository.findById(id).orElseThrow();
        Role role = roleRepository.findOneByName(name).orElseThrow();
        account.getRole().add(role);
        return update(modelMapper.map(account, AccountDto.class));
    }

    @Override
    public AccountDto unassignRoleFromAccount(Long id, String name) {
        Account account = accountRepository.findById(id).orElseThrow();
        Role role = roleRepository.findOneByName(name).orElseThrow();
        account.getRole().remove(role);
        return update(modelMapper.map(account, AccountDto.class));
    }

}
