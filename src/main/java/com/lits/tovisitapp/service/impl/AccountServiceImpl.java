package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.AccountDTO;
import com.lits.tovisitapp.dto.RoleDTO;
import com.lits.tovisitapp.exceptions.account.AccountNotFoundException;
import com.lits.tovisitapp.exceptions.role.RoleNotFoundException;
import com.lits.tovisitapp.model.Account;
import com.lits.tovisitapp.repository.AccountRepository;
import com.lits.tovisitapp.repository.RoleRepository;
import com.lits.tovisitapp.service.AccountService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                              ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        logger.info("Create account with username " + accountDTO.getUsername());
        Account account = modelMapper.map(accountDTO, Account.class);
        account.setPassword(bCryptPasswordEncoder.encode(accountDTO.getPassword()));
        Type listType = new TypeToken<List<Account>>(){}.getType();
        account.setRoles(modelMapper.map(accountDTO.getRoles(), listType));
        return modelMapper.map(accountRepository.save(account), AccountDTO.class);
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        logger.info("Get account with id " + accountId);
        return modelMapper.map(accountRepository.findOneById(accountId).orElseThrow(
                () -> new AccountNotFoundException("There is no account with id " + accountId)), AccountDTO.class);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        logger.info("Get all accounts");
        Type listType = new TypeToken<List<AccountDTO>>(){}.getType();
        return modelMapper.map(accountRepository.findAll(), listType);
    }

    @Override
    @Transactional
    public AccountDTO getAccountByUsername(String username) {
        logger.info("Get account with username " + username);
        return modelMapper.map(accountRepository.findOneByUsername(username).orElseThrow(
                () -> new AccountNotFoundException("There is no account with username " + username)), AccountDTO.class);
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        logger.info(String.format("Update account with id %s, updated account: %s", accountDTO.getId(), accountDTO.toString()));
        Account account = modelMapper.map(accountDTO, Account.class);
        return modelMapper.map(accountRepository.save(account), AccountDTO.class);
    }

    @Override
    public AccountDTO assignRoles(Long accountId, List<Long> roleIds) {
        AccountDTO accountDTO = getAccountById(accountId);
        Type listType = new TypeToken<List<RoleDTO>>(){}.getType();
        List<RoleDTO> roleDTOs = modelMapper.map(roleRepository.findAllByIdIn(roleIds), listType);
        accountDTO.getRoles().addAll(roleDTOs);
        return updateAccount(accountDTO);
    }

    @Override
    public AccountDTO unassignRole(Long accountId, Long roleId) {
        AccountDTO accountDTO = getAccountById(accountId);
        RoleDTO roleToRemove = modelMapper.map(roleRepository.findOneById(roleId).orElseThrow(
                () -> new RoleNotFoundException("There is no role with id " + roleId)), RoleDTO.class);
        accountDTO.getRoles().remove(roleToRemove);
        return updateAccount(accountDTO);
    }

    @Override
    @Transactional
    public List<SimpleGrantedAuthority> getAccountAuthorities(Long accountId) {
        logger.info("Get authorities of account with id " + accountId);
        AccountDTO accountDTO = getAccountById(accountId);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        accountDTO.getRoles().stream().peek(roleDTO -> authorities.add(new SimpleGrantedAuthority(roleDTO.getName())))
                .map(RoleDTO::getPermissions).flatMap(Collection::stream)
                .forEach(permissionDTO -> authorities.add(new SimpleGrantedAuthority(permissionDTO.getName())));
        return authorities;
    }

    @Override
    public void deleteAccount(Long accountId) {
        logger.info("Delete account with id " + accountId);
        accountRepository.deleteById(accountId);
    }
}
