package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.AccountRepository;
import com.lanu.homebudget.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Override
    public Account createAccount(Long userId, Account account) {
        return userService.findByUserId(userId).map(user -> {
            account.setUser(user);
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        return userService.findByUserId(userId)
                .map(user -> accountRepository.findAllByUser(user))
                .orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @Override
    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("AccountId " + accountId + " not found"));
    }

    @Override
    public Account updateAccount(Long accountId, Account requestAccount) {
        return accountRepository.findById(accountId).map(account -> {
            account.setName(requestAccount.getName());
            account.setBalance(requestAccount.getBalance());
            account.setCurrency(requestAccount.getCurrency());
            account.setIncludeInTotal(requestAccount.isIncludeInTotal());
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("AccountId " + accountId + "not found"));
    }

    @Override
    public ResponseEntity<?> deleteAccount(Long accountId) {
        return accountRepository.findById(accountId).map(account -> {
            accountRepository.delete(account);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("AccountId " + accountId + " not found"));
    }
}
